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

describe('CropVariety e2e test', () => {
  const cropVarietyPageUrl = '/crop-variety';
  let username: string;
  let password: string;
  const cropVarietySample = { code: 'qua given', name: 'ouch deer inhibit', active: false };

  let cropVariety;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/crop-varieties+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/crop-varieties').as('postEntityRequest');
    cy.intercept('DELETE', '/api/crop-varieties/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cropVariety) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/crop-varieties/${cropVariety.id}`,
      }).then(() => {
        cropVariety = undefined;
      });
    }
  });

  it('CropVarieties menu should load CropVarieties page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('crop-variety');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CropVariety').should('exist');
    cy.location('pathname').should('eq', cropVarietyPageUrl);
  });

  describe('CropVariety page', () => {
    it('should have translated page title', () => {
      cy.visit(cropVarietyPageUrl);
      cy.getEntityHeading('CropVariety').should('not.contain', 'coopfullApp.cropVariety.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cropVarietyPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CropVariety page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cropVarietyPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CropVariety');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropVarietyPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/crop-varieties',
          body: cropVarietySample,
        }).then(({ body }) => {
          cropVariety = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/crop-varieties+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cropVariety],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cropVarietyPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CropVariety page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cropVariety');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropVarietyPageUrl);
      });

      it('edit button click should load edit CropVariety page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CropVariety');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropVarietyPageUrl);
      });

      it('edit button click should load edit CropVariety page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CropVariety');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropVarietyPageUrl);
      });

      it('last delete button click should delete instance of CropVariety', () => {
        cy.intercept('GET', '/api/crop-varieties/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cropVariety').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cropVarietyPageUrl);

        cropVariety = undefined;
      });
    });
  });

  describe('new CropVariety page', () => {
    beforeEach(() => {
      cy.visit(cropVarietyPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CropVariety');
    });

    it('should create an instance of CropVariety', () => {
      cy.get(`[data-cy="code"]`).type('fervently that');
      cy.get(`[data-cy="code"]`).should('have.value', 'fervently that');

      cy.get(`[data-cy="name"]`).type('patroller');
      cy.get(`[data-cy="name"]`).should('have.value', 'patroller');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="origin"]`).type('whenever silently');
      cy.get(`[data-cy="origin"]`).should('have.value', 'whenever silently');

      cy.get(`[data-cy="maturityDays"]`).type('14058');
      cy.get(`[data-cy="maturityDays"]`).should('have.value', '14058');

      cy.get(`[data-cy="yieldPotential"]`).type('9211.99');
      cy.get(`[data-cy="yieldPotential"]`).should('have.value', '9211.99');

      cy.get(`[data-cy="diseaseResistance"]`).type('funny interesting who');
      cy.get(`[data-cy="diseaseResistance"]`).should('have.value', 'funny interesting who');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cropVariety = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cropVarietyPageUrl);
    });
  });
});
