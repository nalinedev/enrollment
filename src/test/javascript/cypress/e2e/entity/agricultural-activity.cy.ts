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

describe('AgriculturalActivity e2e test', () => {
  const agriculturalActivityPageUrl = '/agricultural-activity';
  let username: string;
  let password: string;
  const agriculturalActivitySample = { totalArea: 30416.79, areaUnit: 'sentimental huzzah weatherize' };

  let agriculturalActivity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agricultural-activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agricultural-activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agricultural-activities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (agriculturalActivity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agricultural-activities/${agriculturalActivity.id}`,
      }).then(() => {
        agriculturalActivity = undefined;
      });
    }
  });

  it('AgriculturalActivities menu should load AgriculturalActivities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agricultural-activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AgriculturalActivity').should('exist');
    cy.location('pathname').should('eq', agriculturalActivityPageUrl);
  });

  describe('AgriculturalActivity page', () => {
    it('should have translated page title', () => {
      cy.visit(agriculturalActivityPageUrl);
      cy.getEntityHeading('AgriculturalActivity').should('not.contain', 'coopfullApp.agriculturalActivity.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agriculturalActivityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AgriculturalActivity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${agriculturalActivityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AgriculturalActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalActivityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agricultural-activities',
          body: agriculturalActivitySample,
        }).then(({ body }) => {
          agriculturalActivity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agricultural-activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [agriculturalActivity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agriculturalActivityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AgriculturalActivity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agriculturalActivity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalActivityPageUrl);
      });

      it('edit button click should load edit AgriculturalActivity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgriculturalActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalActivityPageUrl);
      });

      it('edit button click should load edit AgriculturalActivity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AgriculturalActivity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalActivityPageUrl);
      });

      it('last delete button click should delete instance of AgriculturalActivity', () => {
        cy.intercept('GET', '/api/agricultural-activities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('agriculturalActivity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agriculturalActivityPageUrl);

        agriculturalActivity = undefined;
      });
    });
  });

  describe('new AgriculturalActivity page', () => {
    beforeEach(() => {
      cy.visit(agriculturalActivityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AgriculturalActivity');
    });

    it('should create an instance of AgriculturalActivity', () => {
      cy.get(`[data-cy="totalArea"]`).type('26589.37');
      cy.get(`[data-cy="totalArea"]`).should('have.value', '26589.37');

      cy.get(`[data-cy="areaUnit"]`).type('powerfully kindly bah');
      cy.get(`[data-cy="areaUnit"]`).should('have.value', 'powerfully kindly bah');

      cy.get(`[data-cy="exploitationMode"]`).select('CONTRACTUAL');

      cy.get(`[data-cy="ownershipType"]`).select('OCCUPIED');

      cy.get(`[data-cy="startDate"]`).type('2026-08-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="irrigationAvailable"]`).should('not.be.checked');
      cy.get(`[data-cy="irrigationAvailable"]`).click();
      cy.get(`[data-cy="irrigationAvailable"]`).should('be.checked');

      cy.get(`[data-cy="organicProduction"]`).should('not.be.checked');
      cy.get(`[data-cy="organicProduction"]`).click();
      cy.get(`[data-cy="organicProduction"]`).should('be.checked');

      cy.get(`[data-cy="certification"]`).type('whereas clear');
      cy.get(`[data-cy="certification"]`).should('have.value', 'whereas clear');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        agriculturalActivity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', agriculturalActivityPageUrl);
    });
  });
});
