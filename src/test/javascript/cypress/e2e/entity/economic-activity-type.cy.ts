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

describe('EconomicActivityType e2e test', () => {
  const economicActivityTypePageUrl = '/economic-activity-type';
  let username: string;
  let password: string;
  const economicActivityTypeSample = { code: 'oh enormously', name: 'whose although substitution', active: false };

  let economicActivityType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/economic-activity-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/economic-activity-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/economic-activity-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (economicActivityType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/economic-activity-types/${economicActivityType.id}`,
      }).then(() => {
        economicActivityType = undefined;
      });
    }
  });

  it('EconomicActivityTypes menu should load EconomicActivityTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('economic-activity-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EconomicActivityType').should('exist');
    cy.location('pathname').should('eq', economicActivityTypePageUrl);
  });

  describe('EconomicActivityType page', () => {
    it('should have translated page title', () => {
      cy.visit(economicActivityTypePageUrl);
      cy.getEntityHeading('EconomicActivityType').should('not.contain', 'coopfullApp.economicActivityType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(economicActivityTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EconomicActivityType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${economicActivityTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EconomicActivityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/economic-activity-types',
          body: economicActivityTypeSample,
        }).then(({ body }) => {
          economicActivityType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/economic-activity-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [economicActivityType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(economicActivityTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details EconomicActivityType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('economicActivityType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityTypePageUrl);
      });

      it('edit button click should load edit EconomicActivityType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EconomicActivityType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityTypePageUrl);
      });

      it('edit button click should load edit EconomicActivityType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EconomicActivityType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityTypePageUrl);
      });

      it('last delete button click should delete instance of EconomicActivityType', () => {
        cy.intercept('GET', '/api/economic-activity-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('economicActivityType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', economicActivityTypePageUrl);

        economicActivityType = undefined;
      });
    });
  });

  describe('new EconomicActivityType page', () => {
    beforeEach(() => {
      cy.visit(economicActivityTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EconomicActivityType');
    });

    it('should create an instance of EconomicActivityType', () => {
      cy.get(`[data-cy="code"]`).type('bonfire');
      cy.get(`[data-cy="code"]`).should('have.value', 'bonfire');

      cy.get(`[data-cy="name"]`).type('until behind');
      cy.get(`[data-cy="name"]`).should('have.value', 'until behind');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="sector"]`).type('infinite thoroughly weird');
      cy.get(`[data-cy="sector"]`).should('have.value', 'infinite thoroughly weird');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        economicActivityType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', economicActivityTypePageUrl);
    });
  });
});
