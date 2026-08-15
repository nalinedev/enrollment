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

describe('Cooperative e2e test', () => {
  const cooperativePageUrl = '/cooperative';
  let username: string;
  let password: string;
  const cooperativeSample = { code: 'oxidise', name: 'trouser worth', status: 'ACTIVE', createdDate: '2026-08-15T15:16:09.324Z' };

  let cooperative;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cooperatives+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cooperatives').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cooperatives/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cooperative) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cooperatives/${cooperative.id}`,
      }).then(() => {
        cooperative = undefined;
      });
    }
  });

  it('Cooperatives menu should load Cooperatives page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cooperative');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cooperative').should('exist');
    cy.location('pathname').should('eq', cooperativePageUrl);
  });

  describe('Cooperative page', () => {
    it('should have translated page title', () => {
      cy.visit(cooperativePageUrl);
      cy.getEntityHeading('Cooperative').should('not.contain', 'coopfullApp.cooperative.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cooperativePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cooperative page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cooperativePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Cooperative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cooperatives',
          body: cooperativeSample,
        }).then(({ body }) => {
          cooperative = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cooperatives+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cooperative],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cooperativePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cooperative page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cooperative');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativePageUrl);
      });

      it('edit button click should load edit Cooperative page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cooperative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativePageUrl);
      });

      it('edit button click should load edit Cooperative page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cooperative');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativePageUrl);
      });

      it('last delete button click should delete instance of Cooperative', () => {
        cy.intercept('GET', '/api/cooperatives/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('cooperative').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cooperativePageUrl);

        cooperative = undefined;
      });
    });
  });

  describe('new Cooperative page', () => {
    beforeEach(() => {
      cy.visit(cooperativePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cooperative');
    });

    it('should create an instance of Cooperative', () => {
      cy.get(`[data-cy="code"]`).type('below yahoo forenenst');
      cy.get(`[data-cy="code"]`).should('have.value', 'below yahoo forenenst');

      cy.get(`[data-cy="name"]`).type('general');
      cy.get(`[data-cy="name"]`).should('have.value', 'general');

      cy.get(`[data-cy="legalName"]`).type('toward');
      cy.get(`[data-cy="legalName"]`).should('have.value', 'toward');

      cy.get(`[data-cy="registrationNumber"]`).type('geez');
      cy.get(`[data-cy="registrationNumber"]`).should('have.value', 'geez');

      cy.get(`[data-cy="taxNumber"]`).type('kissingly wherever');
      cy.get(`[data-cy="taxNumber"]`).should('have.value', 'kissingly wherever');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="status"]`).select('CLOSED');

      cy.get(`[data-cy="foundedDate"]`).type('2026-08-15');
      cy.get(`[data-cy="foundedDate"]`).blur();
      cy.get(`[data-cy="foundedDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="email"]`).type('Kristine_Miller@yahoo.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Kristine_Miller@yahoo.com');

      cy.get(`[data-cy="phone"]`).type('406-597-5038 x0207');
      cy.get(`[data-cy="phone"]`).should('have.value', '406-597-5038 x0207');

      cy.get(`[data-cy="website"]`).type('yuck pacemaker');
      cy.get(`[data-cy="website"]`).should('have.value', 'yuck pacemaker');

      cy.get(`[data-cy="createdDate"]`).type('2026-08-15T17:07');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2026-08-15T17:07');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2026-08-15T05:27');
      cy.get(`[data-cy="lastModifiedDate"]`).blur();
      cy.get(`[data-cy="lastModifiedDate"]`).should('have.value', '2026-08-15T05:27');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cooperative = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cooperativePageUrl);
    });
  });
});
