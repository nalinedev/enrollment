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

describe('MembershipApplication e2e test', () => {
  const membershipApplicationPageUrl = '/membership-application';
  let username: string;
  let password: string;
  const membershipApplicationSample = {
    applicationNumber: 'during jungle furthermore',
    status: 'DRAFT',
    applicationDate: '2026-08-15',
    confirmation: false,
  };

  let membershipApplication;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/membership-applications+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/membership-applications').as('postEntityRequest');
    cy.intercept('DELETE', '/api/membership-applications/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (membershipApplication) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/membership-applications/${membershipApplication.id}`,
      }).then(() => {
        membershipApplication = undefined;
      });
    }
  });

  it('MembershipApplications menu should load MembershipApplications page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('membership-application');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MembershipApplication').should('exist');
    cy.location('pathname').should('eq', membershipApplicationPageUrl);
  });

  describe('MembershipApplication page', () => {
    it('should have translated page title', () => {
      cy.visit(membershipApplicationPageUrl);
      cy.getEntityHeading('MembershipApplication').should('not.contain', 'coopfullApp.membershipApplication.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(membershipApplicationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MembershipApplication page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${membershipApplicationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('MembershipApplication');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', membershipApplicationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/membership-applications',
          body: membershipApplicationSample,
        }).then(({ body }) => {
          membershipApplication = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/membership-applications+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [membershipApplication],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(membershipApplicationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MembershipApplication page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('membershipApplication');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', membershipApplicationPageUrl);
      });

      it('edit button click should load edit MembershipApplication page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MembershipApplication');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', membershipApplicationPageUrl);
      });

      it('edit button click should load edit MembershipApplication page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MembershipApplication');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', membershipApplicationPageUrl);
      });

      it('last delete button click should delete instance of MembershipApplication', () => {
        cy.intercept('GET', '/api/membership-applications/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('membershipApplication').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', membershipApplicationPageUrl);

        membershipApplication = undefined;
      });
    });
  });

  describe('new MembershipApplication page', () => {
    beforeEach(() => {
      cy.visit(membershipApplicationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MembershipApplication');
    });

    it('should create an instance of MembershipApplication', () => {
      cy.get(`[data-cy="applicationNumber"]`).type('whoever');
      cy.get(`[data-cy="applicationNumber"]`).should('have.value', 'whoever');

      cy.get(`[data-cy="status"]`).select('REJECTED');

      cy.get(`[data-cy="applicationDate"]`).type('2026-08-15');
      cy.get(`[data-cy="applicationDate"]`).blur();
      cy.get(`[data-cy="applicationDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="submittedAt"]`).type('2026-08-15T02:35');
      cy.get(`[data-cy="submittedAt"]`).blur();
      cy.get(`[data-cy="submittedAt"]`).should('have.value', '2026-08-15T02:35');

      cy.get(`[data-cy="reviewedAt"]`).type('2026-08-15T03:17');
      cy.get(`[data-cy="reviewedAt"]`).blur();
      cy.get(`[data-cy="reviewedAt"]`).should('have.value', '2026-08-15T03:17');

      cy.get(`[data-cy="approvedAt"]`).type('2026-08-15T07:41');
      cy.get(`[data-cy="approvedAt"]`).blur();
      cy.get(`[data-cy="approvedAt"]`).should('have.value', '2026-08-15T07:41');

      cy.get(`[data-cy="rejectedAt"]`).type('2026-08-14T20:39');
      cy.get(`[data-cy="rejectedAt"]`).blur();
      cy.get(`[data-cy="rejectedAt"]`).should('have.value', '2026-08-14T20:39');

      cy.get(`[data-cy="rejectionReason"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="rejectionReason"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="reviewComments"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="reviewComments"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="confirmation"]`).should('not.be.checked');
      cy.get(`[data-cy="confirmation"]`).click();
      cy.get(`[data-cy="confirmation"]`).should('be.checked');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        membershipApplication = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', membershipApplicationPageUrl);
    });
  });
});
