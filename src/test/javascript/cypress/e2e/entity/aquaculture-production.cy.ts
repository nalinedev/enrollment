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

describe('AquacultureProduction e2e test', () => {
  const aquacultureProductionPageUrl = '/aquaculture-production';
  let username: string;
  let password: string;
  const aquacultureProductionSample = { status: 'COMPLETED' };

  let aquacultureProduction;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aquaculture-productions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aquaculture-productions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aquaculture-productions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aquacultureProduction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aquaculture-productions/${aquacultureProduction.id}`,
      }).then(() => {
        aquacultureProduction = undefined;
      });
    }
  });

  it('AquacultureProductions menu should load AquacultureProductions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aquaculture-production');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AquacultureProduction').should('exist');
    cy.location('pathname').should('eq', aquacultureProductionPageUrl);
  });

  describe('AquacultureProduction page', () => {
    it('should have translated page title', () => {
      cy.visit(aquacultureProductionPageUrl);
      cy.getEntityHeading('AquacultureProduction').should('not.contain', 'coopfullApp.aquacultureProduction.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aquacultureProductionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AquacultureProduction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${aquacultureProductionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AquacultureProduction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureProductionPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aquaculture-productions',
          body: aquacultureProductionSample,
        }).then(({ body }) => {
          aquacultureProduction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aquaculture-productions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [aquacultureProduction],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aquacultureProductionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AquacultureProduction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aquacultureProduction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureProductionPageUrl);
      });

      it('edit button click should load edit AquacultureProduction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AquacultureProduction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureProductionPageUrl);
      });

      it('edit button click should load edit AquacultureProduction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AquacultureProduction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureProductionPageUrl);
      });

      it('last delete button click should delete instance of AquacultureProduction', () => {
        cy.intercept('GET', '/api/aquaculture-productions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('aquacultureProduction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureProductionPageUrl);

        aquacultureProduction = undefined;
      });
    });
  });

  describe('new AquacultureProduction page', () => {
    beforeEach(() => {
      cy.visit(aquacultureProductionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AquacultureProduction');
    });

    it('should create an instance of AquacultureProduction', () => {
      cy.get(`[data-cy="productionDate"]`).type('2026-08-15');
      cy.get(`[data-cy="productionDate"]`).blur();
      cy.get(`[data-cy="productionDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="numberOfAnimals"]`).type('7676');
      cy.get(`[data-cy="numberOfAnimals"]`).should('have.value', '7676');

      cy.get(`[data-cy="stockingDensity"]`).type('28542.14');
      cy.get(`[data-cy="stockingDensity"]`).should('have.value', '28542.14');

      cy.get(`[data-cy="productionQuantity"]`).type('10688.29');
      cy.get(`[data-cy="productionQuantity"]`).should('have.value', '10688.29');

      cy.get(`[data-cy="productionUnit"]`).type('amid hastily amazing');
      cy.get(`[data-cy="productionUnit"]`).should('have.value', 'amid hastily amazing');

      cy.get(`[data-cy="averageWeightGrams"]`).type('28745.01');
      cy.get(`[data-cy="averageWeightGrams"]`).should('have.value', '28745.01');

      cy.get(`[data-cy="mortalityCount"]`).type('26775');
      cy.get(`[data-cy="mortalityCount"]`).should('have.value', '26775');

      cy.get(`[data-cy="stockingCount"]`).type('3132');
      cy.get(`[data-cy="stockingCount"]`).should('have.value', '3132');

      cy.get(`[data-cy="harvestedCount"]`).type('20558');
      cy.get(`[data-cy="harvestedCount"]`).should('have.value', '20558');

      cy.get(`[data-cy="expectedProduction"]`).type('20040.37');
      cy.get(`[data-cy="expectedProduction"]`).should('have.value', '20040.37');

      cy.get(`[data-cy="expectedHarvestDate"]`).type('2026-08-15');
      cy.get(`[data-cy="expectedHarvestDate"]`).blur();
      cy.get(`[data-cy="expectedHarvestDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="actualHarvestDate"]`).type('2026-08-15');
      cy.get(`[data-cy="actualHarvestDate"]`).blur();
      cy.get(`[data-cy="actualHarvestDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="status"]`).select('ACTIVE');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        aquacultureProduction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', aquacultureProductionPageUrl);
    });
  });
});
