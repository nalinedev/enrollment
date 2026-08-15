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

describe('LivestockType e2e test', () => {
  const livestockTypePageUrl = '/livestock-type';
  let username: string;
  let password: string;
  const livestockTypeSample = { code: 'proud knowledgeable ew', name: 'heavy making monthly', active: false };

  let livestockType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/livestock-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/livestock-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/livestock-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (livestockType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/livestock-types/${livestockType.id}`,
      }).then(() => {
        livestockType = undefined;
      });
    }
  });

  it('LivestockTypes menu should load LivestockTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('livestock-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LivestockType').should('exist');
    cy.location('pathname').should('eq', livestockTypePageUrl);
  });

  describe('LivestockType page', () => {
    it('should have translated page title', () => {
      cy.visit(livestockTypePageUrl);
      cy.getEntityHeading('LivestockType').should('not.contain', 'coopfullApp.livestockType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(livestockTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LivestockType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${livestockTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('LivestockType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/livestock-types',
          body: livestockTypeSample,
        }).then(({ body }) => {
          livestockType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/livestock-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [livestockType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(livestockTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LivestockType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('livestockType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockTypePageUrl);
      });

      it('edit button click should load edit LivestockType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LivestockType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockTypePageUrl);
      });

      it('edit button click should load edit LivestockType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LivestockType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockTypePageUrl);
      });

      it('last delete button click should delete instance of LivestockType', () => {
        cy.intercept('GET', '/api/livestock-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('livestockType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockTypePageUrl);

        livestockType = undefined;
      });
    });
  });

  describe('new LivestockType page', () => {
    beforeEach(() => {
      cy.visit(livestockTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LivestockType');
    });

    it('should create an instance of LivestockType', () => {
      cy.get(`[data-cy="code"]`).type('popularize');
      cy.get(`[data-cy="code"]`).should('have.value', 'popularize');

      cy.get(`[data-cy="name"]`).type('failing');
      cy.get(`[data-cy="name"]`).should('have.value', 'failing');

      cy.get(`[data-cy="scientificName"]`).type('readmit absent');
      cy.get(`[data-cy="scientificName"]`).should('have.value', 'readmit absent');

      cy.get(`[data-cy="category"]`).type('although queasily save');
      cy.get(`[data-cy="category"]`).should('have.value', 'although queasily save');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        livestockType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', livestockTypePageUrl);
    });
  });
});
