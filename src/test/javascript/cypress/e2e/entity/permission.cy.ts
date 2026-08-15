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

describe('Permission e2e test', () => {
  const permissionPageUrl = '/permission';
  let username: string;
  let password: string;
  const permissionSample = { code: 'blight sense geez', name: 'scrabble', active: true };

  let permission;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/permissions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/permissions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/permissions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (permission) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/permissions/${permission.id}`,
      }).then(() => {
        permission = undefined;
      });
    }
  });

  it('Permissions menu should load Permissions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('permission');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Permission').should('exist');
    cy.location('pathname').should('eq', permissionPageUrl);
  });

  describe('Permission page', () => {
    it('should have translated page title', () => {
      cy.visit(permissionPageUrl);
      cy.getEntityHeading('Permission').should('not.contain', 'coopfullApp.permission.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(permissionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Permission page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${permissionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Permission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', permissionPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/permissions',
          body: permissionSample,
        }).then(({ body }) => {
          permission = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/permissions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [permission],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(permissionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Permission page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('permission');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', permissionPageUrl);
      });

      it('edit button click should load edit Permission page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Permission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', permissionPageUrl);
      });

      it('edit button click should load edit Permission page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Permission');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', permissionPageUrl);
      });

      it('last delete button click should delete instance of Permission', () => {
        cy.intercept('GET', '/api/permissions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('permission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', permissionPageUrl);

        permission = undefined;
      });
    });
  });

  describe('new Permission page', () => {
    beforeEach(() => {
      cy.visit(permissionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Permission');
    });

    it('should create an instance of Permission', () => {
      cy.get(`[data-cy="code"]`).type('exhaust snowplow');
      cy.get(`[data-cy="code"]`).should('have.value', 'exhaust snowplow');

      cy.get(`[data-cy="name"]`).type('dimly');
      cy.get(`[data-cy="name"]`).should('have.value', 'dimly');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="resource"]`).type('handy');
      cy.get(`[data-cy="resource"]`).should('have.value', 'handy');

      cy.get(`[data-cy="action"]`).type('too cumbersome');
      cy.get(`[data-cy="action"]`).should('have.value', 'too cumbersome');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        permission = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', permissionPageUrl);
    });
  });
});
