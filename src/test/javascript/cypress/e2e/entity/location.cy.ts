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

describe('Location e2e test', () => {
  const locationPageUrl = '/location';
  let username: string;
  let password: string;
  const locationSample = { code: 'relieve', name: 'an', type: 'VILLAGE' };

  let location;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/locations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/locations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/locations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (location) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/locations/${location.id}`,
      }).then(() => {
        location = undefined;
      });
    }
  });

  it('Locations menu should load Locations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('location');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Location').should('exist');
    cy.location('pathname').should('eq', locationPageUrl);
  });

  describe('Location page', () => {
    it('should have translated page title', () => {
      cy.visit(locationPageUrl);
      cy.getEntityHeading('Location').should('not.contain', 'coopfullApp.location.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(locationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Location page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${locationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Location');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/locations',
          body: locationSample,
        }).then(({ body }) => {
          location = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/locations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [location],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(locationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Location page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('location');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationPageUrl);
      });

      it('edit button click should load edit Location page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Location');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationPageUrl);
      });

      it('edit button click should load edit Location page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Location');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationPageUrl);
      });

      it('last delete button click should delete instance of Location', () => {
        cy.intercept('GET', '/api/locations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('location').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', locationPageUrl);

        location = undefined;
      });
    });
  });

  describe('new Location page', () => {
    beforeEach(() => {
      cy.visit(locationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Location');
    });

    it('should create an instance of Location', () => {
      cy.get(`[data-cy="code"]`).type('quizzically direct');
      cy.get(`[data-cy="code"]`).should('have.value', 'quizzically direct');

      cy.get(`[data-cy="name"]`).type('obtrude');
      cy.get(`[data-cy="name"]`).should('have.value', 'obtrude');

      cy.get(`[data-cy="type"]`).select('DISTRICT');

      cy.get(`[data-cy="addressLine1"]`).type('sailor');
      cy.get(`[data-cy="addressLine1"]`).should('have.value', 'sailor');

      cy.get(`[data-cy="addressLine2"]`).type('because');
      cy.get(`[data-cy="addressLine2"]`).should('have.value', 'because');

      cy.get(`[data-cy="postalCode"]`).type('nicely inwardly');
      cy.get(`[data-cy="postalCode"]`).should('have.value', 'nicely inwardly');

      cy.get(`[data-cy="latitude"]`).type('15624.45');
      cy.get(`[data-cy="latitude"]`).should('have.value', '15624.45');

      cy.get(`[data-cy="longitude"]`).type('15666.6');
      cy.get(`[data-cy="longitude"]`).should('have.value', '15666.6');

      cy.get(`[data-cy="description"]`).type('father purple stock');
      cy.get(`[data-cy="description"]`).should('have.value', 'father purple stock');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        location = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', locationPageUrl);
    });
  });
});
