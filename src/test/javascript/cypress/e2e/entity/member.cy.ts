import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Member e2e test', () => {
  const memberPageUrl = '/member';
  let username: string;
  let password: string;
  const memberSample = { memberNumber: 'ick', memberType: 'INDIVIDUAL', status: 'CLOSED', createdDate: '2026-08-14T18:37:07.088Z' };

  let member;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/members/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (member) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/members/${member.id}`,
      }).then(() => {
        member = undefined;
      });
    }
  });

  it('Members menu should load Members page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Member').should('exist');
    cy.location('pathname').should('eq', memberPageUrl);
  });

  describe('Member page', () => {
    it('should have translated page title', () => {
      cy.visit(memberPageUrl);
      cy.getEntityHeading('Member').should('not.contain', 'coopfullApp.member.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(memberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Member page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${memberPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Member');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/members',
          body: memberSample,
        }).then(({ body }) => {
          member = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [member],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(memberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Member page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('member');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberPageUrl);
      });

      it('edit button click should load edit Member page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Member');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberPageUrl);
      });

      it('edit button click should load edit Member page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Member');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberPageUrl);
      });

      it('last delete button click should delete instance of Member', () => {
        cy.intercept('GET', '/api/members/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('member').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberPageUrl);

        member = undefined;
      });
    });
  });

  describe('new Member page', () => {
    beforeEach(() => {
      cy.visit(memberPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Member');
    });

    it('should create an instance of Member', () => {
      cy.get(`[data-cy="memberNumber"]`).type('provision finally');
      cy.get(`[data-cy="memberNumber"]`).should('have.value', 'provision finally');

      cy.get(`[data-cy="memberType"]`).select('INDIVIDUAL');

      cy.get(`[data-cy="status"]`).select('INACTIVE');

      cy.get(`[data-cy="admissionDate"]`).type('2026-08-15');
      cy.get(`[data-cy="admissionDate"]`).blur();
      cy.get(`[data-cy="admissionDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="exitDate"]`).type('2026-08-15');
      cy.get(`[data-cy="exitDate"]`).blur();
      cy.get(`[data-cy="exitDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="exitReason"]`).type('or provided');
      cy.get(`[data-cy="exitReason"]`).should('have.value', 'or provided');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="createdDate"]`).type('2026-08-15T07:54');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2026-08-15T07:54');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2026-08-15T10:19');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2026-08-15T10:19');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        member = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', memberPageUrl);
    });
  });
});
