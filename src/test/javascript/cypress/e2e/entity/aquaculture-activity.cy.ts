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

describe('AquacultureActivity e2e test', () => {
  const aquacultureActivityPageUrl = '/aquaculture-activity';
  let username: string;
  let password: string;
  const aquacultureActivitySample = { name: 'submissive', status: 'CLOSED' };

  let aquacultureActivity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aquaculture-activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aquaculture-activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aquaculture-activities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aquacultureActivity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aquaculture-activities/${aquacultureActivity.id}`,
      }).then(() => {
        aquacultureActivity = undefined;
      });
    }
  });

  it('AquacultureActivities menu should load AquacultureActivities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aquaculture-activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AquacultureActivity').should('exist');
    cy.location('pathname').should('eq', aquacultureActivityPageUrl);
  });

  describe('AquacultureActivity page', () => {
    it('should have translated page title', () => {
      cy.visit(aquacultureActivityPageUrl);
      cy.getEntityHeading('AquacultureActivity').should('not.contain', 'coopfullApp.aquacultureActivity.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aquacultureActivityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AquacultureActivity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${aquacultureActivityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AquacultureActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureActivityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aquaculture-activities',
          body: aquacultureActivitySample,
        }).then(({ body }) => {
          aquacultureActivity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aquaculture-activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [aquacultureActivity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aquacultureActivityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AquacultureActivity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aquacultureActivity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureActivityPageUrl);
      });

      it('edit button click should load edit AquacultureActivity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AquacultureActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureActivityPageUrl);
      });

      it('edit button click should load edit AquacultureActivity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AquacultureActivity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureActivityPageUrl);
      });

      it('last delete button click should delete instance of AquacultureActivity', () => {
        cy.intercept('GET', '/api/aquaculture-activities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('aquacultureActivity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquacultureActivityPageUrl);

        aquacultureActivity = undefined;
      });
    });
  });

  describe('new AquacultureActivity page', () => {
    beforeEach(() => {
      cy.visit(aquacultureActivityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AquacultureActivity');
    });

    it('should create an instance of AquacultureActivity', () => {
      cy.get(`[data-cy="name"]`).type('skyscraper pro how');
      cy.get(`[data-cy="name"]`).should('have.value', 'skyscraper pro how');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="productionMode"]`).select('COMMERCIAL');

      cy.get(`[data-cy="ownershipType"]`).select('COMMUNITY');

      cy.get(`[data-cy="productionType"]`).select('COMMERCIAL');

      cy.get(`[data-cy="systemType"]`).select('POND');

      cy.get(`[data-cy="startDate"]`).type('2026-08-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="totalArea"]`).type('31679.75');
      cy.get(`[data-cy="totalArea"]`).should('have.value', '31679.75');

      cy.get(`[data-cy="areaUnit"]`).type('table overburden');
      cy.get(`[data-cy="areaUnit"]`).should('have.value', 'table overburden');

      cy.get(`[data-cy="waterSource"]`).type('lay pop contravene');
      cy.get(`[data-cy="waterSource"]`).should('have.value', 'lay pop contravene');

      cy.get(`[data-cy="numberOfProductionUnits"]`).type('20636');
      cy.get(`[data-cy="numberOfProductionUnits"]`).should('have.value', '20636');

      cy.get(`[data-cy="productionUnitDescription"]`).type('deflate');
      cy.get(`[data-cy="productionUnitDescription"]`).should('have.value', 'deflate');

      cy.get(`[data-cy="status"]`).select('ACTIVE');

      cy.get(`[data-cy="annualRevenue"]`).type('7285.41');
      cy.get(`[data-cy="annualRevenue"]`).should('have.value', '7285.41');

      cy.get(`[data-cy="monthlyRevenue"]`).type('29738.04');
      cy.get(`[data-cy="monthlyRevenue"]`).should('have.value', '29738.04');

      cy.get(`[data-cy="employees"]`).type('14290');
      cy.get(`[data-cy="employees"]`).should('have.value', '14290');

      cy.get(`[data-cy="certification"]`).type('guidance plus');
      cy.get(`[data-cy="certification"]`).should('have.value', 'guidance plus');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        aquacultureActivity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', aquacultureActivityPageUrl);
    });
  });
});
