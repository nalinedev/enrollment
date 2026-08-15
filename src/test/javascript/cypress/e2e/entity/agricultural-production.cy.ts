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

describe('AgriculturalProduction e2e test', () => {
  const agriculturalProductionPageUrl = '/agricultural-production';
  let username: string;
  let password: string;
  const agriculturalProductionSample = { area: 29106.8, areaUnit: 'once sign', status: 'ACTIVE' };

  let agriculturalProduction;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agricultural-productions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agricultural-productions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agricultural-productions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agriculturalProduction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agricultural-productions/${agriculturalProduction.id}`,
      }).then(() => {
        agriculturalProduction = undefined;
      });
    }
  });

  it('AgriculturalProductions menu should load AgriculturalProductions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agricultural-production');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgriculturalProduction').should('exist');
    cy.location('pathname').should('eq', agriculturalProductionPageUrl);
  });

  describe('AgriculturalProduction page', () => {
    it('should have translated page title', () => {
      cy.visit(agriculturalProductionPageUrl);
      cy.getEntityHeading('AgriculturalProduction').should('not.contain', 'coopfullApp.agriculturalProduction.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agriculturalProductionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgriculturalProduction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${agriculturalProductionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AgriculturalProduction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalProductionPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agricultural-productions',
          body: agriculturalProductionSample,
        }).then(({ body }) => {
          agriculturalProduction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agricultural-productions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agriculturalProduction],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agriculturalProductionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgriculturalProduction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agriculturalProduction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalProductionPageUrl);
      });

      it('edit button click should load edit AgriculturalProduction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgriculturalProduction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalProductionPageUrl);
      });

      it('edit button click should load edit AgriculturalProduction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgriculturalProduction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalProductionPageUrl);
      });

      it('last delete button click should delete instance of AgriculturalProduction', () => {
        cy.intercept('GET', '/api/agricultural-productions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agriculturalProduction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalProductionPageUrl);

        agriculturalProduction = undefined;
      });
    });
  });

  describe('new AgriculturalProduction page', () => {
    beforeEach(() => {
      cy.visit(agriculturalProductionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AgriculturalProduction');
    });

    it('should create an instance of AgriculturalProduction', () => {
      cy.get(`[data-cy="area"]`).type('22055.33');
      cy.get(`[data-cy="area"]`).should('have.value', '22055.33');

      cy.get(`[data-cy="areaUnit"]`).type('unnecessarily oof pro');
      cy.get(`[data-cy="areaUnit"]`).should('have.value', 'unnecessarily oof pro');

      cy.get(`[data-cy="plantingDate"]`).type('2026-08-14');
      cy.get(`[data-cy="plantingDate"]`).blur();
      cy.get(`[data-cy="plantingDate"]`).should('have.value', '2026-08-14');

      cy.get(`[data-cy="harvestStartDate"]`).type('2026-08-15');
      cy.get(`[data-cy="harvestStartDate"]`).blur();
      cy.get(`[data-cy="harvestStartDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="harvestEndDate"]`).type('2026-08-15');
      cy.get(`[data-cy="harvestEndDate"]`).blur();
      cy.get(`[data-cy="harvestEndDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="productionQuantity"]`).type('18681.61');
      cy.get(`[data-cy="productionQuantity"]`).should('have.value', '18681.61');

      cy.get(`[data-cy="productionUnit"]`).type('joyfully willfully');
      cy.get(`[data-cy="productionUnit"]`).should('have.value', 'joyfully willfully');

      cy.get(`[data-cy="expectedAnnualProduction"]`).type('32269.3');
      cy.get(`[data-cy="expectedAnnualProduction"]`).should('have.value', '32269.3');

      cy.get(`[data-cy="numberOfPlants"]`).type('22053');
      cy.get(`[data-cy="numberOfPlants"]`).should('have.value', '22053');

      cy.get(`[data-cy="plantingDensity"]`).type('8735.09');
      cy.get(`[data-cy="plantingDensity"]`).should('have.value', '8735.09');

      cy.get(`[data-cy="productionYear"]`).type('7988');
      cy.get(`[data-cy="productionYear"]`).should('have.value', '7988');

      cy.get(`[data-cy="status"]`).select('ACTIVE');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        agriculturalProduction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', agriculturalProductionPageUrl);
    });
  });
});
