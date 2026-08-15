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

describe('EconomicActivity e2e test', () => {
  const economicActivityPageUrl = '/economic-activity';
  let username: string;
  let password: string;
  const economicActivitySample = { name: 'vibraphone after awkwardly', mainActivity: false, status: 'SUSPENDED' };

  let economicActivity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/economic-activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/economic-activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/economic-activities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (economicActivity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/economic-activities/${economicActivity.id}`,
      }).then(() => {
        economicActivity = undefined;
      });
    }
  });

  it('EconomicActivities menu should load EconomicActivities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('economic-activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EconomicActivity').should('exist');
    cy.location('pathname').should('eq', economicActivityPageUrl);
  });

  describe('EconomicActivity page', () => {
    it('should have translated page title', () => {
      cy.visit(economicActivityPageUrl);
      cy.getEntityHeading('EconomicActivity').should('not.contain', 'coopfullApp.economicActivity.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(economicActivityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EconomicActivity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${economicActivityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EconomicActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/economic-activities',
          body: economicActivitySample,
        }).then(({ body }) => {
          economicActivity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/economic-activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [economicActivity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(economicActivityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EconomicActivity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('economicActivity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityPageUrl);
      });

      it('edit button click should load edit EconomicActivity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EconomicActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityPageUrl);
      });

      it('edit button click should load edit EconomicActivity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EconomicActivity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityPageUrl);
      });

      it('last delete button click should delete instance of EconomicActivity', () => {
        cy.intercept('GET', '/api/economic-activities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('economicActivity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityPageUrl);

        economicActivity = undefined;
      });
    });
  });

  describe('new EconomicActivity page', () => {
    beforeEach(() => {
      cy.visit(economicActivityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EconomicActivity');
    });

    it('should create an instance of EconomicActivity', () => {
      cy.get(`[data-cy="name"]`).type('ack collaboration');
      cy.get(`[data-cy="name"]`).should('have.value', 'ack collaboration');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="mainActivity"]`).should('not.be.checked');
      cy.get(`[data-cy="mainActivity"]`).click();
      cy.get(`[data-cy="mainActivity"]`).should('be.checked');

      cy.get(`[data-cy="startDate"]`).type('2026-08-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="endDate"]`).type('2026-08-15');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="annualRevenue"]`).type('9606.49');
      cy.get(`[data-cy="annualRevenue"]`).should('have.value', '9606.49');

      cy.get(`[data-cy="monthlyRevenue"]`).type('30578.12');
      cy.get(`[data-cy="monthlyRevenue"]`).should('have.value', '30578.12');

      cy.get(`[data-cy="numberOfEmployees"]`).type('31922');
      cy.get(`[data-cy="numberOfEmployees"]`).should('have.value', '31922');

      cy.get(`[data-cy="status"]`).select('ACTIVE');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        economicActivity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', economicActivityPageUrl);
    });
  });
});
