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

describe('AuditLog e2e test', () => {
  const auditLogPageUrl = '/audit-log';
  let username: string;
  let password: string;
  const auditLogSample = { action: 'SUBMIT', entityName: 'likely ha', timestamp: '2026-08-15T13:25:58.125Z' };

  let auditLog;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/audit-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/audit-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/audit-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (auditLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/audit-logs/${auditLog.id}`,
      }).then(() => {
        auditLog = undefined;
      });
    }
  });

  it('AuditLogs menu should load AuditLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('audit-log');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AuditLog').should('exist');
    cy.location('pathname').should('eq', auditLogPageUrl);
  });

  describe('AuditLog page', () => {
    it('should have translated page title', () => {
      cy.visit(auditLogPageUrl);
      cy.getEntityHeading('AuditLog').should('not.contain', 'coopfullApp.auditLog.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(auditLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AuditLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${auditLogPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AuditLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', auditLogPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/audit-logs',
          body: auditLogSample,
        }).then(({ body }) => {
          auditLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/audit-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [auditLog],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(auditLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AuditLog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('auditLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', auditLogPageUrl);
      });

      it('edit button click should load edit AuditLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AuditLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', auditLogPageUrl);
      });

      it('edit button click should load edit AuditLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AuditLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', auditLogPageUrl);
      });

      it('last delete button click should delete instance of AuditLog', () => {
        cy.intercept('GET', '/api/audit-logs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('auditLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', auditLogPageUrl);

        auditLog = undefined;
      });
    });
  });

  describe('new AuditLog page', () => {
    beforeEach(() => {
      cy.visit(auditLogPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AuditLog');
    });

    it('should create an instance of AuditLog', () => {
      cy.get(`[data-cy="action"]`).select('REJECT');

      cy.get(`[data-cy="entityName"]`).type('icebreaker concerning duh');
      cy.get(`[data-cy="entityName"]`).should('have.value', 'icebreaker concerning duh');

      cy.get(`[data-cy="entityId"]`).type('like');
      cy.get(`[data-cy="entityId"]`).should('have.value', 'like');

      cy.get(`[data-cy="username"]`).type('quiet ack');
      cy.get(`[data-cy="username"]`).should('have.value', 'quiet ack');

      cy.get(`[data-cy="cooperativeId"]`).type('15637');
      cy.get(`[data-cy="cooperativeId"]`).should('have.value', '15637');

      cy.get(`[data-cy="branchId"]`).type('11694');
      cy.get(`[data-cy="branchId"]`).should('have.value', '11694');

      cy.get(`[data-cy="timestamp"]`).type('2026-08-15T08:57');
      cy.get(`[data-cy="timestamp"]`).blur();
      cy.get(`[data-cy="timestamp"]`).should('have.value', '2026-08-15T08:57');

      cy.get(`[data-cy="ipAddress"]`).type('yowza serene');
      cy.get(`[data-cy="ipAddress"]`).should('have.value', 'yowza serene');

      cy.get(`[data-cy="userAgent"]`).type('airline');
      cy.get(`[data-cy="userAgent"]`).should('have.value', 'airline');

      cy.get(`[data-cy="oldValue"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="oldValue"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="newValue"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="newValue"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        auditLog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', auditLogPageUrl);
    });
  });
});
