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

describe('CooperativeRole e2e test', () => {
  const cooperativeRolePageUrl = '/cooperative-role';
  let username: string;
  let password: string;
  const cooperativeRoleSample = { code: 'boohoo phew', name: 'phooey', status: 'INACTIVE' };

  let cooperativeRole;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cooperative-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cooperative-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cooperative-roles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cooperativeRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cooperative-roles/${cooperativeRole.id}`,
      }).then(() => {
        cooperativeRole = undefined;
      });
    }
  });

  it('CooperativeRoles menu should load CooperativeRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cooperative-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CooperativeRole').should('exist');
    cy.location('pathname').should('eq', cooperativeRolePageUrl);
  });

  describe('CooperativeRole page', () => {
    it('should have translated page title', () => {
      cy.visit(cooperativeRolePageUrl);
      cy.getEntityHeading('CooperativeRole').should('not.contain', 'coopfullApp.cooperativeRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cooperativeRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CooperativeRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cooperativeRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CooperativeRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cooperative-roles',
          body: cooperativeRoleSample,
        }).then(({ body }) => {
          cooperativeRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cooperative-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cooperativeRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cooperativeRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CooperativeRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cooperativeRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeRolePageUrl);
      });

      it('edit button click should load edit CooperativeRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CooperativeRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeRolePageUrl);
      });

      it('edit button click should load edit CooperativeRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CooperativeRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeRolePageUrl);
      });

      it('last delete button click should delete instance of CooperativeRole', () => {
        cy.intercept('GET', '/api/cooperative-roles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cooperativeRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeRolePageUrl);

        cooperativeRole = undefined;
      });
    });
  });

  describe('new CooperativeRole page', () => {
    beforeEach(() => {
      cy.visit(cooperativeRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CooperativeRole');
    });

    it('should create an instance of CooperativeRole', () => {
      cy.get(`[data-cy="code"]`).type('warmhearted amongst uh-huh');
      cy.get(`[data-cy="code"]`).should('have.value', 'warmhearted amongst uh-huh');

      cy.get(`[data-cy="name"]`).type('softly despite resource');
      cy.get(`[data-cy="name"]`).should('have.value', 'softly despite resource');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="status"]`).select('ACTIVE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cooperativeRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cooperativeRolePageUrl);
    });
  });
});
