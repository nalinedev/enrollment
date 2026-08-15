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

describe('OrganizationMember e2e test', () => {
  const organizationMemberPageUrl = '/organization-member';
  let username: string;
  let password: string;
  const organizationMemberSample = { legalName: 'fooey quit' };

  let organizationMember;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/organization-members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/organization-members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/organization-members/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (organizationMember) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/organization-members/${organizationMember.id}`,
      }).then(() => {
        organizationMember = undefined;
      });
    }
  });

  it('OrganizationMembers menu should load OrganizationMembers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('organization-member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OrganizationMember').should('exist');
    cy.location('pathname').should('eq', organizationMemberPageUrl);
  });

  describe('OrganizationMember page', () => {
    it('should have translated page title', () => {
      cy.visit(organizationMemberPageUrl);
      cy.getEntityHeading('OrganizationMember').should('not.contain', 'coopfullApp.organizationMember.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(organizationMemberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OrganizationMember page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${organizationMemberPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('OrganizationMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', organizationMemberPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/organization-members',
          body: organizationMemberSample,
        }).then(({ body }) => {
          organizationMember = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/organization-members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [organizationMember],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(organizationMemberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OrganizationMember page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('organizationMember');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', organizationMemberPageUrl);
      });

      it('edit button click should load edit OrganizationMember page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', organizationMemberPageUrl);
      });

      it('edit button click should load edit OrganizationMember page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OrganizationMember');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', organizationMemberPageUrl);
      });

      it('last delete button click should delete instance of OrganizationMember', () => {
        cy.intercept('GET', '/api/organization-members/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('organizationMember').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', organizationMemberPageUrl);

        organizationMember = undefined;
      });
    });
  });

  describe('new OrganizationMember page', () => {
    beforeEach(() => {
      cy.visit(organizationMemberPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OrganizationMember');
    });

    it('should create an instance of OrganizationMember', () => {
      cy.get(`[data-cy="legalName"]`).type('clearly whoa');
      cy.get(`[data-cy="legalName"]`).should('have.value', 'clearly whoa');

      cy.get(`[data-cy="tradeName"]`).type('gadzooks narrowcast');
      cy.get(`[data-cy="tradeName"]`).should('have.value', 'gadzooks narrowcast');

      cy.get(`[data-cy="registrationNumber"]`).type('silently');
      cy.get(`[data-cy="registrationNumber"]`).should('have.value', 'silently');

      cy.get(`[data-cy="taxNumber"]`).type('stranger');
      cy.get(`[data-cy="taxNumber"]`).should('have.value', 'stranger');

      cy.get(`[data-cy="legalForm"]`).type('cultivated versus apropos');
      cy.get(`[data-cy="legalForm"]`).should('have.value', 'cultivated versus apropos');

      cy.get(`[data-cy="registrationDate"]`).type('2026-08-14');
      cy.get(`[data-cy="registrationDate"]`).blur();
      cy.get(`[data-cy="registrationDate"]`).should('have.value', '2026-08-14');

      cy.get(`[data-cy="email"]`).type('Whitney_Williamson30@yahoo.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Whitney_Williamson30@yahoo.com');

      cy.get(`[data-cy="phoneNumber"]`).type('drat');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'drat');

      cy.get(`[data-cy="website"]`).type('pinion an gah');
      cy.get(`[data-cy="website"]`).should('have.value', 'pinion an gah');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        organizationMember = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', organizationMemberPageUrl);
    });
  });
});
