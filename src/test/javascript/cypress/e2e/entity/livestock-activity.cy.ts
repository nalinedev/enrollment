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

describe('LivestockActivity e2e test', () => {
  const livestockActivityPageUrl = '/livestock-activity';
  let username: string;
  let password: string;
  const livestockActivitySample = { name: 'hm on soon', status: 'ACTIVE' };

  let livestockActivity;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/livestock-activities+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/livestock-activities').as('postEntityRequest');
    cy.intercept('DELETE', '/api/livestock-activities/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (livestockActivity) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/livestock-activities/${livestockActivity.id}`,
      }).then(() => {
        livestockActivity = undefined;
      });
    }
  });

  it('LivestockActivities menu should load LivestockActivities page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('livestock-activity');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LivestockActivity').should('exist');
    cy.location('pathname').should('eq', livestockActivityPageUrl);
  });

  describe('LivestockActivity page', () => {
    it('should have translated page title', () => {
      cy.visit(livestockActivityPageUrl);
      cy.getEntityHeading('LivestockActivity').should('not.contain', 'coopfullApp.livestockActivity.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(livestockActivityPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LivestockActivity page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${livestockActivityPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('LivestockActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockActivityPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/livestock-activities',
          body: livestockActivitySample,
        }).then(({ body }) => {
          livestockActivity = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/livestock-activities+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [livestockActivity],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(livestockActivityPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LivestockActivity page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('livestockActivity');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockActivityPageUrl);
      });

      it('edit button click should load edit LivestockActivity page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LivestockActivity');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockActivityPageUrl);
      });

      it('edit button click should load edit LivestockActivity page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LivestockActivity');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockActivityPageUrl);
      });

      it('last delete button click should delete instance of LivestockActivity', () => {
        cy.intercept('GET', '/api/livestock-activities/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('livestockActivity').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', livestockActivityPageUrl);

        livestockActivity = undefined;
      });
    });
  });

  describe('new LivestockActivity page', () => {
    beforeEach(() => {
      cy.visit(livestockActivityPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LivestockActivity');
    });

    it('should create an instance of LivestockActivity', () => {
      cy.get(`[data-cy="name"]`).type('reward');
      cy.get(`[data-cy="name"]`).should('have.value', 'reward');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="productionMode"]`).select('INDIVIDUAL');

      cy.get(`[data-cy="ownershipType"]`).select('SHARED');

      cy.get(`[data-cy="productionType"]`).select('EGGS');

      cy.get(`[data-cy="startDate"]`).type('2026-08-15');
      cy.get(`[data-cy="startDate"]`).blur();
      cy.get(`[data-cy="startDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="totalArea"]`).type('14789.16');
      cy.get(`[data-cy="totalArea"]`).should('have.value', '14789.16');

      cy.get(`[data-cy="areaUnit"]`).type('gah coast whether');
      cy.get(`[data-cy="areaUnit"]`).should('have.value', 'gah coast whether');

      cy.get(`[data-cy="status"]`).select('CLOSED');

      cy.get(`[data-cy="numberOfAnimals"]`).type('25422');
      cy.get(`[data-cy="numberOfAnimals"]`).should('have.value', '25422');

      cy.get(`[data-cy="annualRevenue"]`).type('32278.31');
      cy.get(`[data-cy="annualRevenue"]`).should('have.value', '32278.31');

      cy.get(`[data-cy="monthlyRevenue"]`).type('31218.32');
      cy.get(`[data-cy="monthlyRevenue"]`).should('have.value', '31218.32');

      cy.get(`[data-cy="employees"]`).type('25066');
      cy.get(`[data-cy="employees"]`).should('have.value', '25066');

      cy.get(`[data-cy="veterinaryServiceAvailable"]`).should('not.be.checked');
      cy.get(`[data-cy="veterinaryServiceAvailable"]`).click();
      cy.get(`[data-cy="veterinaryServiceAvailable"]`).should('be.checked');

      cy.get(`[data-cy="feedSource"]`).type('ascribe stunt');
      cy.get(`[data-cy="feedSource"]`).should('have.value', 'ascribe stunt');

      cy.get(`[data-cy="waterSource"]`).type('strategy foolishly archaeology');
      cy.get(`[data-cy="waterSource"]`).should('have.value', 'strategy foolishly archaeology');

      cy.get(`[data-cy="certification"]`).type('wealthy sympathetically provided');
      cy.get(`[data-cy="certification"]`).should('have.value', 'wealthy sympathetically provided');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        livestockActivity = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', livestockActivityPageUrl);
    });
  });
});
