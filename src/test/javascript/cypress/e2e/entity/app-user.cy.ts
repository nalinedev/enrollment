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

describe('AppUser e2e test', () => {
  const appUserPageUrl = '/app-user';
  let username: string;
  let password: string;
  const appUserSample = { status: 'SUSPENDED', createdDate: '2026-08-14T22:21:04.370Z' };

  let appUser;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/app-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/app-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/app-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (appUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/app-users/${appUser.id}`,
      }).then(() => {
        appUser = undefined;
      });
    }
  });

  it('AppUsers menu should load AppUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('app-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AppUser').should('exist');
    cy.location('pathname').should('eq', appUserPageUrl);
  });

  describe('AppUser page', () => {
    it('should have translated page title', () => {
      cy.visit(appUserPageUrl);
      cy.getEntityHeading('AppUser').should('not.contain', 'coopfullApp.appUser.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(appUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AppUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${appUserPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AppUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', appUserPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/app-users',
          body: appUserSample,
        }).then(({ body }) => {
          appUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/app-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [appUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(appUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AppUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('appUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', appUserPageUrl);
      });

      it('edit button click should load edit AppUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AppUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', appUserPageUrl);
      });

      it('edit button click should load edit AppUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AppUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', appUserPageUrl);
      });

      it('last delete button click should delete instance of AppUser', () => {
        cy.intercept('GET', '/api/app-users/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('appUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', appUserPageUrl);

        appUser = undefined;
      });
    });
  });

  describe('new AppUser page', () => {
    beforeEach(() => {
      cy.visit(appUserPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AppUser');
    });

    it('should create an instance of AppUser', () => {
      cy.get(`[data-cy="employeeNumber"]`).type('unto');
      cy.get(`[data-cy="employeeNumber"]`).should('have.value', 'unto');

      cy.get(`[data-cy="firstName"]`).type('Natasha');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Natasha');

      cy.get(`[data-cy="lastName"]`).type('Kunze');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Kunze');

      cy.get(`[data-cy="phoneNumber"]`).type('instead since mid');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'instead since mid');

      cy.get(`[data-cy="jobTitle"]`).type('Internal Data Facilitator');
      cy.get(`[data-cy="jobTitle"]`).should('have.value', 'Internal Data Facilitator');

      cy.get(`[data-cy="department"]`).type('circle meh shore');
      cy.get(`[data-cy="department"]`).should('have.value', 'circle meh shore');

      cy.get(`[data-cy="status"]`).select('LOCKED');

      cy.get(`[data-cy="createdDate"]`).type('2026-08-15T17:54');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2026-08-15T17:54');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2026-08-15T04:59');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2026-08-15T04:59');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        appUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', appUserPageUrl);
    });
  });
});
