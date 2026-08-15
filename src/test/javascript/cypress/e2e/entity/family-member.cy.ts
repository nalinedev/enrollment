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

describe('FamilyMember e2e test', () => {
  const familyMemberPageUrl = '/family-member';
  let username: string;
  let password: string;
  const familyMemberSample = { firstName: 'Neal', lastName: 'Zulauf', relationship: 'because' };

  let familyMember;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/family-members+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/family-members').as('postEntityRequest');
    cy.intercept('DELETE', '/api/family-members/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (familyMember) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/family-members/${familyMember.id}`,
      }).then(() => {
        familyMember = undefined;
      });
    }
  });

  it('FamilyMembers menu should load FamilyMembers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('family-member');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FamilyMember').should('exist');
    cy.location('pathname').should('eq', familyMemberPageUrl);
  });

  describe('FamilyMember page', () => {
    it('should have translated page title', () => {
      cy.visit(familyMemberPageUrl);
      cy.getEntityHeading('FamilyMember').should('not.contain', 'coopfullApp.familyMember.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(familyMemberPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FamilyMember page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${familyMemberPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('FamilyMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', familyMemberPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/family-members',
          body: familyMemberSample,
        }).then(({ body }) => {
          familyMember = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/family-members+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [familyMember],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(familyMemberPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FamilyMember page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('familyMember');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', familyMemberPageUrl);
      });

      it('edit button click should load edit FamilyMember page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FamilyMember');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', familyMemberPageUrl);
      });

      it('edit button click should load edit FamilyMember page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FamilyMember');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', familyMemberPageUrl);
      });

      it('last delete button click should delete instance of FamilyMember', () => {
        cy.intercept('GET', '/api/family-members/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('familyMember').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', familyMemberPageUrl);

        familyMember = undefined;
      });
    });
  });

  describe('new FamilyMember page', () => {
    beforeEach(() => {
      cy.visit(familyMemberPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FamilyMember');
    });

    it('should create an instance of FamilyMember', () => {
      cy.get(`[data-cy="firstName"]`).type('Earnest');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Earnest');

      cy.get(`[data-cy="middleName"]`).type('over');
      cy.get(`[data-cy="middleName"]`).should('have.value', 'over');

      cy.get(`[data-cy="lastName"]`).type('Oberbrunner');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Oberbrunner');

      cy.get(`[data-cy="relationship"]`).type('likewise shrilly lost');
      cy.get(`[data-cy="relationship"]`).should('have.value', 'likewise shrilly lost');

      cy.get(`[data-cy="gender"]`).type('save');
      cy.get(`[data-cy="gender"]`).should('have.value', 'save');

      cy.get(`[data-cy="birthDate"]`).type('2026-08-15');
      cy.get(`[data-cy="birthDate"]`).blur();
      cy.get(`[data-cy="birthDate"]`).should('have.value', '2026-08-15');

      cy.get(`[data-cy="birthPlace"]`).type('impassioned');
      cy.get(`[data-cy="birthPlace"]`).should('have.value', 'impassioned');

      cy.get(`[data-cy="nationality"]`).type('nicely apropos');
      cy.get(`[data-cy="nationality"]`).should('have.value', 'nicely apropos');

      cy.get(`[data-cy="phoneNumber"]`).type('wisecrack');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'wisecrack');

      cy.get(`[data-cy="occupation"]`).type('wherever and');
      cy.get(`[data-cy="occupation"]`).should('have.value', 'wherever and');

      cy.get(`[data-cy="dependent"]`).should('not.be.checked');
      cy.get(`[data-cy="dependent"]`).click();
      cy.get(`[data-cy="dependent"]`).should('be.checked');

      cy.get(`[data-cy="notes"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="notes"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        familyMember = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', familyMemberPageUrl);
    });
  });
});
