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

describe('LivestockProduction e2e test', () => {
  const livestockProductionPageUrl = '/livestock-production';
  let username: string;
  let password: string;
  const livestockProductionSample = { numberOfAnimals: 21176, status: 'OTHER' };

  let livestockProduction;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/livestock-productions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/livestock-productions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/livestock-productions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (livestockProduction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/livestock-productions/${livestockProduction.id}`,
      }).then(() => {
        livestockProduction = undefined;
      });
    }
  });

  it('LivestockProductions menu should load LivestockProductions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('livestock-production');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LivestockProduction').should('exist');
    cy.location('pathname').should('eq', livestockProductionPageUrl);
  });

  describe('LivestockProduction page', () => {
    it('should have translated page title', () => {
      cy.visit(livestockProductionPageUrl);
      cy.getEntityHeading('LivestockProduction').should('not.contain', 'coopfullApp.livestockProduction.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(livestockProductionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LivestockProduction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${livestockProductionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('LivestockProduction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockProductionPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/livestock-productions',
          body: livestockProductionSample,
        }).then(({ body }) => {
          livestockProduction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/livestock-productions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [livestockProduction],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(livestockProductionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LivestockProduction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('livestockProduction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockProductionPageUrl);
      });

      it('edit button click should load edit LivestockProduction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LivestockProduction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockProductionPageUrl);
      });

      it('edit button click should load edit LivestockProduction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LivestockProduction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockProductionPageUrl);
      });

      it('last delete button click should delete instance of LivestockProduction', () => {
        cy.intercept('GET', '/api/livestock-productions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('livestockProduction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockProductionPageUrl);

        livestockProduction = undefined;
      });
    });
  });

  describe('new LivestockProduction page', () => {
    beforeEach(() => {
      cy.visit(livestockProductionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LivestockProduction');
    });

    it('should create an instance of LivestockProduction', () => {
      cy.get(`[data-cy="productionDate"]`).type('2026-08-15');
      cy.get(`[data-cy="productionDate"]`).blur();
      cy.get(`[data-cy="productionDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="animalSex"]`).select('MALE');

      cy.get(`[data-cy="numberOfAnimals"]`).type('28210');
      cy.get(`[data-cy="numberOfAnimals"]`).should('have.value', '28210');

      cy.get(`[data-cy="averageAgeMonths"]`).type('25215');
      cy.get(`[data-cy="averageAgeMonths"]`).should('have.value', '25215');

      cy.get(`[data-cy="averageWeightKg"]`).type('6303.24');
      cy.get(`[data-cy="averageWeightKg"]`).should('have.value', '6303.24');

      cy.get(`[data-cy="productionQuantity"]`).type('20005.28');
      cy.get(`[data-cy="productionQuantity"]`).should('have.value', '20005.28');

      cy.get(`[data-cy="productionUnit"]`).type('really');
      cy.get(`[data-cy="productionUnit"]`).should('have.value', 'really');

      cy.get(`[data-cy="mortalityCount"]`).type('15861');
      cy.get(`[data-cy="mortalityCount"]`).should('have.value', '15861');

      cy.get(`[data-cy="birthCount"]`).type('17170');
      cy.get(`[data-cy="birthCount"]`).should('have.value', '17170');

      cy.get(`[data-cy="soldCount"]`).type('10370');
      cy.get(`[data-cy="soldCount"]`).should('have.value', '10370');

      cy.get(`[data-cy="status"]`).select('DEAD');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        livestockProduction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', livestockProductionPageUrl);
    });
  });
});
