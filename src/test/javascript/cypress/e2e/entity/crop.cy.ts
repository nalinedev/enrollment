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

describe('Crop e2e test', () => {
  const cropPageUrl = '/crop';
  let username: string;
  let password: string;
  const cropSample = { code: 'repeatedly', name: 'focused', active: false };

  let crop;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crops+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crops').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crops/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (crop) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crops/${crop.id}`,
      }).then(() => {
        crop = undefined;
      });
    }
  });

  it('Crops menu should load Crops page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crop');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Crop').should('exist');
    cy.location('pathname').should('eq', cropPageUrl);
  });

  describe('Crop page', () => {
    it('should have translated page title', () => {
      cy.visit(cropPageUrl);
      cy.getEntityHeading('Crop').should('not.contain', 'coopfullApp.crop.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cropPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Crop page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cropPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Crop');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crops',
          body: cropSample,
        }).then(({ body }) => {
          crop = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crops+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [crop],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cropPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Crop page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('crop');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropPageUrl);
      });

      it('edit button click should load edit Crop page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Crop');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropPageUrl);
      });

      it('edit button click should load edit Crop page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Crop');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropPageUrl);
      });

      it('last delete button click should delete instance of Crop', () => {
        cy.intercept('GET', '/api/crops/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('crop').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropPageUrl);

        crop = undefined;
      });
    });
  });

  describe('new Crop page', () => {
    beforeEach(() => {
      cy.visit(cropPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Crop');
    });

    it('should create an instance of Crop', () => {
      cy.get(`[data-cy="code"]`).type('ew');
      cy.get(`[data-cy="code"]`).should('have.value', 'ew');

      cy.get(`[data-cy="name"]`).type('gloss dense ah');
      cy.get(`[data-cy="name"]`).should('have.value', 'gloss dense ah');

      cy.get(`[data-cy="scientificName"]`).type('so');
      cy.get(`[data-cy="scientificName"]`).should('have.value', 'so');

      cy.get(`[data-cy="category"]`).type('though');
      cy.get(`[data-cy="category"]`).should('have.value', 'though');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="perennial"]`).should('not.be.checked');
      cy.get(`[data-cy="perennial"]`).click();
      cy.get(`[data-cy="perennial"]`).should('be.checked');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        crop = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cropPageUrl);
    });
  });
});
