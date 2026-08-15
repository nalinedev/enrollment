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

describe('SocialProfile e2e test', () => {
  const socialProfilePageUrl = '/social-profile';
  let username: string;
  let password: string;
  const socialProfileSample = {};

  let socialProfile;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/social-profiles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/social-profiles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/social-profiles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (socialProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/social-profiles/${socialProfile.id}`,
      }).then(() => {
        socialProfile = undefined;
      });
    }
  });

  it('SocialProfiles menu should load SocialProfiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('social-profile');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SocialProfile').should('exist');
    cy.location('pathname').should('eq', socialProfilePageUrl);
  });

  describe('SocialProfile page', () => {
    it('should have translated page title', () => {
      cy.visit(socialProfilePageUrl);
      cy.getEntityHeading('SocialProfile').should('not.contain', 'coopfullApp.socialProfile.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(socialProfilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SocialProfile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${socialProfilePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('SocialProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', socialProfilePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/social-profiles',
          body: socialProfileSample,
        }).then(({ body }) => {
          socialProfile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/social-profiles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [socialProfile],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(socialProfilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SocialProfile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('socialProfile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', socialProfilePageUrl);
      });

      it('edit button click should load edit SocialProfile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', socialProfilePageUrl);
      });

      it('edit button click should load edit SocialProfile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SocialProfile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', socialProfilePageUrl);
      });

      it('last delete button click should delete instance of SocialProfile', () => {
        cy.intercept('GET', '/api/social-profiles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('socialProfile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', socialProfilePageUrl);

        socialProfile = undefined;
      });
    });
  });

  describe('new SocialProfile page', () => {
    beforeEach(() => {
      cy.visit(socialProfilePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SocialProfile');
    });

    it('should create an instance of SocialProfile', () => {
      cy.get(`[data-cy="maritalStatus"]`).select('UNKNOWN');

      cy.get(`[data-cy="numberOfChildren"]`).type('31532');
      cy.get(`[data-cy="numberOfChildren"]`).should('have.value', '31532');

      cy.get(`[data-cy="numberOfDependents"]`).type('15799');
      cy.get(`[data-cy="numberOfDependents"]`).should('have.value', '15799');

      cy.get(`[data-cy="educationLevel"]`).type('indeed upwardly rally');
      cy.get(`[data-cy="educationLevel"]`).should('have.value', 'indeed upwardly rally');

      cy.get(`[data-cy="housingStatus"]`).type('shrill gadzooks well');
      cy.get(`[data-cy="housingStatus"]`).should('have.value', 'shrill gadzooks well');

      cy.get(`[data-cy="residenceSince"]`).type('2026-08-15');
      cy.get(`[data-cy="residenceSince"]`).blur();
      cy.get(`[data-cy="residenceSince"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="disabilityStatus"]`).should('not.be.checked');
      cy.get(`[data-cy="disabilityStatus"]`).click();
      cy.get(`[data-cy="disabilityStatus"]`).should('be.checked');

      cy.get(`[data-cy="disabilityDescription"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="disabilityDescription"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="socialCategory"]`).type('yippee if horde');
      cy.get(`[data-cy="socialCategory"]`).should('have.value', 'yippee if horde');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        socialProfile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', socialProfilePageUrl);
    });
  });
});
