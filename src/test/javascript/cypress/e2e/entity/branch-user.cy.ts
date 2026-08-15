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

describe('BranchUser e2e test', () => {
  const branchUserPageUrl = '/branch-user';
  let username: string;
  let password: string;
  const branchUserSample = { startDate: '2026-08-15', active: true };

  let branchUser;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/branch-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/branch-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/branch-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (branchUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/branch-users/${branchUser.id}`,
      }).then(() => {
        branchUser = undefined;
      });
    }
  });

  it('BranchUsers menu should load BranchUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('branch-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BranchUser').should('exist');
    cy.location('pathname').should('eq', branchUserPageUrl);
  });

  describe('BranchUser page', () => {
    it('should have translated page title', () => {
      cy.visit(branchUserPageUrl);
      cy.getEntityHeading('BranchUser').should('not.contain', 'coopfullApp.branchUser.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(branchUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BranchUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${branchUserPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('BranchUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', branchUserPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/branch-users',
          body: branchUserSample,
        }).then(({ body }) => {
          branchUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/branch-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [branchUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(branchUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BranchUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('branchUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', branchUserPageUrl);
      });

      it('edit button click should load edit BranchUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BranchUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', branchUserPageUrl);
      });

      it('edit button click should load edit BranchUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BranchUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', branchUserPageUrl);
      });

      it('last delete button click should delete instance of BranchUser', () => {
        cy.intercept('GET', '/api/branch-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('branchUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', branchUserPageUrl);

        branchUser = undefined;
      });
    });
  });

  describe('new BranchUser page', () => {
    beforeEach(() => {
      cy.visit(branchUserPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BranchUser');
    });

    it('should create an instance of BranchUser', () => {
      cy.get(`[data-cy="startDate"]`).type('2026-08-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="endDate"]`).type('2026-08-14');
      cy.get(`[data-cy="endDate"]`).blur();
      cy.get(`[data-cy="endDate"]`).should('have.value', '2026-08-14');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        branchUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', branchUserPageUrl);
    });
  });
});
