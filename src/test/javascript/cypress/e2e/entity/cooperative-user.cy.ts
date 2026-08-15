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

describe('CooperativeUser e2e test', () => {
  const cooperativeUserPageUrl = '/cooperative-user';
  let username: string;
  let password: string;
  const cooperativeUserSample = { startDate: '2026-08-15', active: true };

  let cooperativeUser;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cooperative-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cooperative-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cooperative-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cooperativeUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cooperative-users/${cooperativeUser.id}`,
      }).then(() => {
        cooperativeUser = undefined;
      });
    }
  });

  it('CooperativeUsers menu should load CooperativeUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cooperative-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CooperativeUser').should('exist');
    cy.location('pathname').should('eq', cooperativeUserPageUrl);
  });

  describe('CooperativeUser page', () => {
    it('should have translated page title', () => {
      cy.visit(cooperativeUserPageUrl);
      cy.getEntityHeading('CooperativeUser').should('not.contain', 'coopfullApp.cooperativeUser.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cooperativeUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CooperativeUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cooperativeUserPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CooperativeUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeUserPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cooperative-users',
          body: cooperativeUserSample,
        }).then(({ body }) => {
          cooperativeUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cooperative-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cooperativeUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cooperativeUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CooperativeUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cooperativeUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeUserPageUrl);
      });

      it('edit button click should load edit CooperativeUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CooperativeUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeUserPageUrl);
      });

      it('edit button click should load edit CooperativeUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CooperativeUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeUserPageUrl);
      });

      it('last delete button click should delete instance of CooperativeUser', () => {
        cy.intercept('GET', '/api/cooperative-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cooperativeUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeUserPageUrl);

        cooperativeUser = undefined;
      });
    });
  });

  describe('new CooperativeUser page', () => {
    beforeEach(() => {
      cy.visit(cooperativeUserPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CooperativeUser');
    });

    it('should create an instance of CooperativeUser', () => {
      cy.get(`[data-cy="startDate"]`).type('2026-08-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="endDate"]`).type('2026-08-15');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cooperativeUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cooperativeUserPageUrl);
    });
  });
});
