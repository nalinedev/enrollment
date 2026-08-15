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

describe('AquaticSpecies e2e test', () => {
  const aquaticSpeciesPageUrl = '/aquatic-species';
  let username: string;
  let password: string;
  const aquaticSpeciesSample = { code: 'sleet', name: 'devise timely', active: false };

  let aquaticSpecies;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/aquatic-species+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/aquatic-species').as('postEntityRequest');
    cy.intercept('DELETE', '/api/aquatic-species/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (aquaticSpecies) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/aquatic-species/${aquaticSpecies.id}`,
      }).then(() => {
        aquaticSpecies = undefined;
      });
    }
  });

  it('AquaticSpecieses menu should load AquaticSpecieses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('aquatic-species');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AquaticSpecies').should('exist');
    cy.location('pathname').should('eq', aquaticSpeciesPageUrl);
  });

  describe('AquaticSpecies page', () => {
    it('should have translated page title', () => {
      cy.visit(aquaticSpeciesPageUrl);
      cy.getEntityHeading('AquaticSpecies').should('not.contain', 'coopfullApp.aquaticSpecies.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(aquaticSpeciesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AquaticSpecies page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${aquaticSpeciesPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AquaticSpecies');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquaticSpeciesPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/aquatic-species',
          body: aquaticSpeciesSample,
        }).then(({ body }) => {
          aquaticSpecies = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/aquatic-species+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [aquaticSpecies],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(aquaticSpeciesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AquaticSpecies page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('aquaticSpecies');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquaticSpeciesPageUrl);
      });

      it('edit button click should load edit AquaticSpecies page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AquaticSpecies');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquaticSpeciesPageUrl);
      });

      it('edit button click should load edit AquaticSpecies page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AquaticSpecies');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquaticSpeciesPageUrl);
      });

      it('last delete button click should delete instance of AquaticSpecies', () => {
        cy.intercept('GET', '/api/aquatic-species/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('aquaticSpecies').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', aquaticSpeciesPageUrl);

        aquaticSpecies = undefined;
      });
    });
  });

  describe('new AquaticSpecies page', () => {
    beforeEach(() => {
      cy.visit(aquaticSpeciesPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AquaticSpecies');
    });

    it('should create an instance of AquaticSpecies', () => {
      cy.get(`[data-cy="code"]`).type('yum decode blah');
      cy.get(`[data-cy="code"]`).should('have.value', 'yum decode blah');

      cy.get(`[data-cy="name"]`).type('rudely');
      cy.get(`[data-cy="name"]`).should('have.value', 'rudely');

      cy.get(`[data-cy="scientificName"]`).type('barring including supposing');
      cy.get(`[data-cy="scientificName"]`).should('have.value', 'barring including supposing');

      cy.get(`[data-cy="category"]`).type('bend yuck yuck');
      cy.get(`[data-cy="category"]`).should('have.value', 'bend yuck yuck');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="freshwater"]`).should('not.be.checked');
      cy.get(`[data-cy="freshwater"]`).click();
      cy.get(`[data-cy="freshwater"]`).should('be.checked');

      cy.get(`[data-cy="saltwater"]`).should('not.be.checked');
      cy.get(`[data-cy="saltwater"]`).click();
      cy.get(`[data-cy="saltwater"]`).should('be.checked');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        aquaticSpecies = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', aquaticSpeciesPageUrl);
    });
  });
});
