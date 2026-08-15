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

describe('CooperativeBranch e2e test', () => {
  const cooperativeBranchPageUrl = '/cooperative-branch';
  let username: string;
  let password: string;
  const cooperativeBranchSample = { code: 'bliss daintily', name: 'baggy', status: 'SUSPENDED' };

  let cooperativeBranch;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cooperative-branches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cooperative-branches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cooperative-branches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cooperativeBranch) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cooperative-branches/${cooperativeBranch.id}`,
      }).then(() => {
        cooperativeBranch = undefined;
      });
    }
  });

  it('CooperativeBranches menu should load CooperativeBranches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cooperative-branch');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CooperativeBranch').should('exist');
    cy.location('pathname').should('eq', cooperativeBranchPageUrl);
  });

  describe('CooperativeBranch page', () => {
    it('should have translated page title', () => {
      cy.visit(cooperativeBranchPageUrl);
      cy.getEntityHeading('CooperativeBranch').should('not.contain', 'coopfullApp.cooperativeBranch.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cooperativeBranchPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CooperativeBranch page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cooperativeBranchPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CooperativeBranch');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeBranchPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cooperative-branches',
          body: cooperativeBranchSample,
        }).then(({ body }) => {
          cooperativeBranch = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cooperative-branches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cooperativeBranch],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cooperativeBranchPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CooperativeBranch page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cooperativeBranch');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeBranchPageUrl);
      });

      it('edit button click should load edit CooperativeBranch page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CooperativeBranch');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeBranchPageUrl);
      });

      it('edit button click should load edit CooperativeBranch page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CooperativeBranch');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeBranchPageUrl);
      });

      it('last delete button click should delete instance of CooperativeBranch', () => {
        cy.intercept('GET', '/api/cooperative-branches/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cooperativeBranch').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativeBranchPageUrl);

        cooperativeBranch = undefined;
      });
    });
  });

  describe('new CooperativeBranch page', () => {
    beforeEach(() => {
      cy.visit(cooperativeBranchPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CooperativeBranch');
    });

    it('should create an instance of CooperativeBranch', () => {
      cy.get(`[data-cy="code"]`).type('pro');
      cy.get(`[data-cy="code"]`).should('have.value', 'pro');

      cy.get(`[data-cy="name"]`).type('sandy');
      cy.get(`[data-cy="name"]`).should('have.value', 'sandy');

      cy.get(`[data-cy="description"]`).type('whereas who idealistic');
      cy.get(`[data-cy="description"]`).should('have.value', 'whereas who idealistic');

      cy.get(`[data-cy="phone"]`).type('(473) 231-2078 x72098');
      cy.get(`[data-cy="phone"]`).should('have.value', '(473) 231-2078 x72098');

      cy.get(`[data-cy="email"]`).type('Deontae_Gerlach46@yahoo.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Deontae_Gerlach46@yahoo.com');

      cy.get(`[data-cy="status"]`).select('ACTIVE');

      cy.get(`[data-cy="openingDate"]`).type('2026-08-14');
      cy.get(`[data-cy="openingDate"]`).blur();
      cy.get(`[data-cy="openingDate"]`).should('have.value', '2026-08-14');

      cy.get(`[data-cy="closingDate"]`).type('2026-08-14');
      cy.get(`[data-cy="closingDate"]`).blur();
      cy.get(`[data-cy="closingDate"]`).should('have.value', '2026-08-14');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cooperativeBranch = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cooperativeBranchPageUrl);
    });
  });
});
