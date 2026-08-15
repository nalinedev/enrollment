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

describe('MemberDocument e2e test', () => {
  const memberDocumentPageUrl = '/member-document';
  let username: string;
  let password: string;
  const memberDocumentSample = { documentType: 'PASSPORT', verificationStatus: 'PENDING', uploadedAt: '2026-08-15T10:16:06.594Z' };

  let memberDocument;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/member-documents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/member-documents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/member-documents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (memberDocument) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/member-documents/${memberDocument.id}`,
      }).then(() => {
        memberDocument = undefined;
      });
    }
  });

  it('MemberDocuments menu should load MemberDocuments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('member-document');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MemberDocument').should('exist');
    cy.location('pathname').should('eq', memberDocumentPageUrl);
  });

  describe('MemberDocument page', () => {
    it('should have translated page title', () => {
      cy.visit(memberDocumentPageUrl);
      cy.getEntityHeading('MemberDocument').should('not.contain', 'coopfullApp.memberDocument.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(memberDocumentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MemberDocument page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${memberDocumentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('MemberDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberDocumentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/member-documents',
          body: memberDocumentSample,
        }).then(({ body }) => {
          memberDocument = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/member-documents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [memberDocument],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(memberDocumentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MemberDocument page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('memberDocument');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberDocumentPageUrl);
      });

      it('edit button click should load edit MemberDocument page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MemberDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberDocumentPageUrl);
      });

      it('edit button click should load edit MemberDocument page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MemberDocument');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberDocumentPageUrl);
      });

      it('last delete button click should delete instance of MemberDocument', () => {
        cy.intercept('GET', '/api/member-documents/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('memberDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', memberDocumentPageUrl);

        memberDocument = undefined;
      });
    });
  });

  describe('new MemberDocument page', () => {
    beforeEach(() => {
      cy.visit(memberDocumentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MemberDocument');
    });

    it('should create an instance of MemberDocument', () => {
      cy.get(`[data-cy="documentType"]`).select('MARRIAGE_CERTIFICATE');

      cy.get(`[data-cy="originalFileName"]`).type('seagull acidly contravene');
      cy.get(`[data-cy="originalFileName"]`).should('have.value', 'seagull acidly contravene');

      cy.get(`[data-cy="storedFileName"]`).type('accelerator');
      cy.get(`[data-cy="storedFileName"]`).should('have.value', 'accelerator');

      cy.get(`[data-cy="contentType"]`).type('fake gazebo');
      cy.get(`[data-cy="contentType"]`).should('have.value', 'fake gazebo');

      cy.get(`[data-cy="fileSize"]`).type('9019');
      cy.get(`[data-cy="fileSize"]`).should('have.value', '9019');

      cy.get(`[data-cy="storagePath"]`).type('hm');
      cy.get(`[data-cy="storagePath"]`).should('have.value', 'hm');

      cy.get(`[data-cy="checksum"]`).type('shout oof');
      cy.get(`[data-cy="checksum"]`).should('have.value', 'shout oof');

      cy.get(`[data-cy="verificationStatus"]`).select('PENDING');

      cy.get(`[data-cy="uploadedAt"]`).type('2026-08-14T22:24');
      cy.get(`[data-cy="uploadedAt"]`).blur();
      cy.get(`[data-cy="uploadedAt"]`).should('have.value', '2026-08-14T22:24');

      cy.get(`[data-cy="verifiedAt"]`).type('2026-08-14T20:30');
      cy.get(`[data-cy="verifiedAt"]`).blur();
      cy.get(`[data-cy="verifiedAt"]`).should('have.value', '2026-08-14T20:30');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        memberDocument = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', memberDocumentPageUrl);
    });
  });
});
