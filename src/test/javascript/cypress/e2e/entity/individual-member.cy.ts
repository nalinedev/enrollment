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

describe('IndividualMember e2e test', () => {
  const individualMemberPageUrl = '/individual-member';
  let username: string;
  let password: string;
  const individualMemberSample = { firstName: 'Melba', lastName: 'Murphy' };

  let individualMember;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/individual-members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/individual-members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/individual-members/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (individualMember) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/individual-members/${individualMember.id}`,
      }).then(() => {
        individualMember = undefined;
      });
    }
  });

  it('IndividualMembers menu should load IndividualMembers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('individual-member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IndividualMember').should('exist');
    cy.location('pathname').should('eq', individualMemberPageUrl);
  });

  describe('IndividualMember page', () => {
    it('should have translated page title', () => {
      cy.visit(individualMemberPageUrl);
      cy.getEntityHeading('IndividualMember').should('not.contain', 'coopfullApp.individualMember.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(individualMemberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IndividualMember page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${individualMemberPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('IndividualMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', individualMemberPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/individual-members',
          body: individualMemberSample,
        }).then(({ body }) => {
          individualMember = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/individual-members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [individualMember],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(individualMemberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IndividualMember page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('individualMember');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', individualMemberPageUrl);
      });

      it('edit button click should load edit IndividualMember page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IndividualMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', individualMemberPageUrl);
      });

      it('edit button click should load edit IndividualMember page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IndividualMember');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', individualMemberPageUrl);
      });

      it('last delete button click should delete instance of IndividualMember', () => {
        cy.intercept('GET', '/api/individual-members/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('individualMember').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', individualMemberPageUrl);

        individualMember = undefined;
      });
    });
  });

  describe('new IndividualMember page', () => {
    beforeEach(() => {
      cy.visit(individualMemberPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IndividualMember');
    });

    it('should create an instance of IndividualMember', () => {
      cy.get(`[data-cy="firstName"]`).type('Tammy');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Tammy');

      cy.get(`[data-cy="middleName"]`).type('duh infinite amid');
      cy.get(`[data-cy="middleName"]`).should('have.value', 'duh infinite amid');

      cy.get(`[data-cy="lastName"]`).type('Rippin');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Rippin');

      cy.get(`[data-cy="maidenName"]`).type('present manipulate aha');
      cy.get(`[data-cy="maidenName"]`).should('have.value', 'present manipulate aha');

      cy.get(`[data-cy="gender"]`).type('restructure director');
      cy.get(`[data-cy="gender"]`).should('have.value', 'restructure director');

      cy.get(`[data-cy="birthDate"]`).type('2026-08-14');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2026-08-14');

      cy.get(`[data-cy="birthPlace"]`).type('another meaningfully presume');
      cy.get(`[data-cy="birthPlace"]`).should('have.value', 'another meaningfully presume');

      cy.get(`[data-cy="nationality"]`).type('pish tusk sharply');
      cy.get(`[data-cy="nationality"]`).should('have.value', 'pish tusk sharply');

      cy.get(`[data-cy="email"]`).type('Adelbert.Von@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Adelbert.Von@hotmail.com');

      cy.get(`[data-cy="phoneNumber"]`).type('amidst whereas');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'amidst whereas');

      cy.get(`[data-cy="occupation"]`).type('bah aha');
      cy.get(`[data-cy="occupation"]`).should('have.value', 'bah aha');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        individualMember = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', individualMemberPageUrl);
    });
  });
});
