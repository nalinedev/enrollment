import agriculturalActivity from 'app/entities/agricultural-activity/agricultural-activity.reducer';
import agriculturalProduction from 'app/entities/agricultural-production/agricultural-production.reducer';
import appUser from 'app/entities/app-user/app-user.reducer';
import aquacultureActivity from 'app/entities/aquaculture-activity/aquaculture-activity.reducer';
import aquacultureProduction from 'app/entities/aquaculture-production/aquaculture-production.reducer';
import aquaticSpecies from 'app/entities/aquatic-species/aquatic-species.reducer';
import auditLog from 'app/entities/audit-log/audit-log.reducer';
import branchUser from 'app/entities/branch-user/branch-user.reducer';
import cooperative from 'app/entities/cooperative/cooperative.reducer';
import cooperativeBranch from 'app/entities/cooperative-branch/cooperative-branch.reducer';
import individualMember from 'app/entities/individual-member/individual-member.reducer';
import location from 'app/entities/location/location.reducer';
import member from 'app/entities/member/member.reducer';
import membershipApplication from 'app/entities/membership-application/membership-application.reducer';
import numberSequence from 'app/entities/number-sequence/number-sequence.reducer';
import organizationMember from 'app/entities/organization-member/organization-member.reducer';
import socialProfile from 'app/entities/social-profile/social-profile.reducer';
import familyMember from 'app/entities/family-member/family-member.reducer';
import professionalProfile from 'app/entities/professional-profile/professional-profile.reducer';
import identityDocument from 'app/entities/identity-document/identity-document.reducer';
import memberDocument from 'app/entities/member-document/member-document.reducer';
import economicActivityType from 'app/entities/economic-activity-type/economic-activity-type.reducer';
import economicActivity from 'app/entities/economic-activity/economic-activity.reducer';
import crop from 'app/entities/crop/crop.reducer';
import cropVariety from 'app/entities/crop-variety/crop-variety.reducer';
import livestockType from 'app/entities/livestock-type/livestock-type.reducer';
import livestockActivity from 'app/entities/livestock-activity/livestock-activity.reducer';
import livestockProduction from 'app/entities/livestock-production/livestock-production.reducer';
import cooperativeRole from 'app/entities/cooperative-role/cooperative-role.reducer';
import cooperativeUser from 'app/entities/cooperative-user/cooperative-user.reducer';
import permission from 'app/entities/permission/permission.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  cooperative,
  location,
  cooperativeBranch,
  member,
  individualMember,
  organizationMember,
  membershipApplication,
  socialProfile,
  familyMember,
  professionalProfile,
  identityDocument,
  memberDocument,
  economicActivityType,
  economicActivity,
  agriculturalActivity,
  crop,
  cropVariety,
  agriculturalProduction,
  livestockType,
  livestockActivity,
  livestockProduction,
  aquaticSpecies,
  aquacultureActivity,
  aquacultureProduction,
  appUser,
  cooperativeRole,
  cooperativeUser,
  branchUser,
  permission,
  auditLog,
  numberSequence,
  // jhipster-needle-add-reducer-combine - JHipster will add reducer here
};

export default entitiesReducers;
