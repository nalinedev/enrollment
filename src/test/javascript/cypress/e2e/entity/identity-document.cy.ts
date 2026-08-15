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

describe('IdentityDocument e2e test', () => {
  const identityDocumentPageUrl = '/identity-document';
  let username: string;
  let password: string;
  const identityDocumentSample = { documentType: 'hairy motionless superb', documentNumber: 'huzzah', status: 'LOST', verified: true };

  let identityDocument;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/identity-documents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/identity-documents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/identity-documents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (identityDocument) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/identity-documents/${identityDocument.id}`,
      }).then(() => {
        identityDocument = undefined;
      });
    }
  });

  it('IdentityDocuments menu should load IdentityDocuments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('identity-document');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IdentityDocument').should('exist');
    cy.location('pathname').should('eq', identityDocumentPageUrl);
  });

  describe('IdentityDocument page', () => {
    it('should have translated page title', () => {
      cy.visit(identityDocumentPageUrl);
      cy.getEntityHeading('IdentityDocument').should('not.contain', 'coopfullApp.identityDocument.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(identityDocumentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IdentityDocument page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${identityDocumentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', identityDocumentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/identity-documents',
          body: identityDocumentSample,
        }).then(({ body }) => {
          identityDocument = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/identity-documents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [identityDocument],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(identityDocumentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IdentityDocument page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('identityDocument');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', identityDocumentPageUrl);
      });

      it('edit button click should load edit IdentityDocument page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', identityDocumentPageUrl);
      });

      it('edit button click should load edit IdentityDocument page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IdentityDocument');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', identityDocumentPageUrl);
      });

      it('last delete button click should delete instance of IdentityDocument', () => {
        cy.intercept('GET', '/api/identity-documents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('identityDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', identityDocumentPageUrl);

        identityDocument = undefined;
      });
    });
  });

  describe('new IdentityDocument page', () => {
    beforeEach(() => {
      cy.visit(identityDocumentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IdentityDocument');
    });

    it('should create an instance of IdentityDocument', () => {
      cy.get(`[data-cy="documentType"]`).type('aw butter');
      cy.get(`[data-cy="documentType"]`).should('have.value', 'aw butter');

      cy.get(`[data-cy="documentNumber"]`).type('claw');
      cy.get(`[data-cy="documentNumber"]`).should('have.value', 'claw');

      cy.get(`[data-cy="issueDate"]`).type('2026-08-15');
      cy.get(`[data-cy="issueDate"]`).blur();
      cy.get(`[data-cy="issueDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="expiryDate"]`).type('2026-08-15');
      cy.get(`[data-cy="expiryDate"]`).blur();
      cy.get(`[data-cy="expiryDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="issuingAuthority"]`).type('that into');
      cy.get(`[data-cy="issuingAuthority"]`).should('have.value', 'that into');

      cy.get(`[data-cy="issuingCountry"]`).type('considering');
      cy.get(`[data-cy="issuingCountry"]`).should('have.value', 'considering');

      cy.get(`[data-cy="status"]`).select('CANCELLED');

      cy.get(`[data-cy="verified"]`).should('not.be.checked');
      cy.get(`[data-cy="verified"]`).click();
      cy.get(`[data-cy="verified"]`).should('be.checked');

      cy.get(`[data-cy="verificationDate"]`).type('2026-08-15T07:44');
      cy.get(`[data-cy="verificationDate"]`).blur();
      cy.get(`[data-cy="verificationDate"]`).should('have.value', '2026-08-15T07:44');

      cy.get(`[data-cy="verificationComment"]`).type('how above');
      cy.get(`[data-cy="verificationComment"]`).should('have.value', 'how above');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        identityDocument = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', identityDocumentPageUrl);
    });
  });
});
