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

describe('NumberSequence e2e test', () => {
  const numberSequencePageUrl = '/number-sequence';
  let username: string;
  let password: string;
  const numberSequenceSample = { sequenceType: 'DOCUMENT', currentValue: 25255, padding: 12568 };

  let numberSequence;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/number-sequences+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/number-sequences').as('postEntityRequest');
    cy.intercept('DELETE', '/api/number-sequences/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (numberSequence) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/number-sequences/${numberSequence.id}`,
      }).then(() => {
        numberSequence = undefined;
      });
    }
  });

  it('NumberSequences menu should load NumberSequences page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('number-sequence');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NumberSequence').should('exist');
    cy.location('pathname').should('eq', numberSequencePageUrl);
  });

  describe('NumberSequence page', () => {
    it('should have translated page title', () => {
      cy.visit(numberSequencePageUrl);
      cy.getEntityHeading('NumberSequence').should('not.contain', 'coopfullApp.numberSequence.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(numberSequencePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NumberSequence page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${numberSequencePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('NumberSequence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', numberSequencePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/number-sequences',
          body: numberSequenceSample,
        }).then(({ body }) => {
          numberSequence = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/number-sequences+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [numberSequence],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(numberSequencePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NumberSequence page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('numberSequence');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', numberSequencePageUrl);
      });

      it('edit button click should load edit NumberSequence page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NumberSequence');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', numberSequencePageUrl);
      });

      it('edit button click should load edit NumberSequence page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NumberSequence');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', numberSequencePageUrl);
      });

      it('last delete button click should delete instance of NumberSequence', () => {
        cy.intercept('GET', '/api/number-sequences/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('numberSequence').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', numberSequencePageUrl);

        numberSequence = undefined;
      });
    });
  });

  describe('new NumberSequence page', () => {
    beforeEach(() => {
      cy.visit(numberSequencePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('NumberSequence');
    });

    it('should create an instance of NumberSequence', () => {
      cy.get(`[data-cy="sequenceType"]`).select('MEMBER');

      cy.get(`[data-cy="prefix"]`).type('for truly');
      cy.get(`[data-cy="prefix"]`).should('have.value', 'for truly');

      cy.get(`[data-cy="year"]`).type('2633');
      cy.get(`[data-cy="year"]`).should('have.value', '2633');

      cy.get(`[data-cy="currentValue"]`).type('24926');
      cy.get(`[data-cy="currentValue"]`).should('have.value', '24926');

      cy.get(`[data-cy="padding"]`).type('764');
      cy.get(`[data-cy="padding"]`).should('have.value', '764');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        numberSequence = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', numberSequencePageUrl);
    });
  });
});
