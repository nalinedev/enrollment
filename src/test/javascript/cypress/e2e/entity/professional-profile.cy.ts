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

describe('ProfessionalProfile e2e test', () => {
  const professionalProfilePageUrl = '/professional-profile';
  let username: string;
  let password: string;
  const professionalProfileSample = {};

  let professionalProfile;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/professional-profiles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/professional-profiles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/professional-profiles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (professionalProfile) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/professional-profiles/${professionalProfile.id}`,
      }).then(() => {
        professionalProfile = undefined;
      });
    }
  });

  it('ProfessionalProfiles menu should load ProfessionalProfiles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('professional-profile');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProfessionalProfile').should('exist');
    cy.location('pathname').should('eq', professionalProfilePageUrl);
  });

  describe('ProfessionalProfile page', () => {
    it('should have translated page title', () => {
      cy.visit(professionalProfilePageUrl);
      cy.getEntityHeading('ProfessionalProfile').should('not.contain', 'coopfullApp.professionalProfile.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(professionalProfilePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProfessionalProfile page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${professionalProfilePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProfessionalProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', professionalProfilePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/professional-profiles',
          body: professionalProfileSample,
        }).then(({ body }) => {
          professionalProfile = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/professional-profiles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [professionalProfile],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(professionalProfilePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProfessionalProfile page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('professionalProfile');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', professionalProfilePageUrl);
      });

      it('edit button click should load edit ProfessionalProfile page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProfessionalProfile');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', professionalProfilePageUrl);
      });

      it('edit button click should load edit ProfessionalProfile page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProfessionalProfile');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', professionalProfilePageUrl);
      });

      it('last delete button click should delete instance of ProfessionalProfile', () => {
        cy.intercept('GET', '/api/professional-profiles/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('professionalProfile').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', professionalProfilePageUrl);

        professionalProfile = undefined;
      });
    });
  });

  describe('new ProfessionalProfile page', () => {
    beforeEach(() => {
      cy.visit(professionalProfilePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProfessionalProfile');
    });

    it('should create an instance of ProfessionalProfile', () => {
      cy.get(`[data-cy="employmentStatus"]`).type('er forgather');
      cy.get(`[data-cy="employmentStatus"]`).should('have.value', 'er forgather');

      cy.get(`[data-cy="employerName"]`).type('than');
      cy.get(`[data-cy="employerName"]`).should('have.value', 'than');

      cy.get(`[data-cy="jobTitle"]`).type('District Creative Officer');
      cy.get(`[data-cy="jobTitle"]`).should('have.value', 'District Creative Officer');

      cy.get(`[data-cy="profession"]`).type('with because optimistically');
      cy.get(`[data-cy="profession"]`).should('have.value', 'with because optimistically');

      cy.get(`[data-cy="sector"]`).type('translation earth rarely');
      cy.get(`[data-cy="sector"]`).should('have.value', 'translation earth rarely');

      cy.get(`[data-cy="yearsOfExperience"]`).type('25581');
      cy.get(`[data-cy="yearsOfExperience"]`).should('have.value', '25581');

      cy.get(`[data-cy="monthlyIncome"]`).type('13364.33');
      cy.get(`[data-cy="monthlyIncome"]`).should('have.value', '13364.33');

      cy.get(`[data-cy="annualIncome"]`).type('22822.86');
      cy.get(`[data-cy="annualIncome"]`).should('have.value', '22822.86');

      cy.get(`[data-cy="employmentStartDate"]`).type('2026-08-15');
      cy.get(`[data-cy="employmentStartDate"]`).blur();
      cy.get(`[data-cy="employmentStartDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="employerLocation"]`).type('kick');
      cy.get(`[data-cy="employerLocation"]`).should('have.value', 'kick');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        professionalProfile = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', professionalProfilePageUrl);
    });
  });
});
