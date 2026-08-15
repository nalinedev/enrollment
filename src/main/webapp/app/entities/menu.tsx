import React from 'react';
import { Translate } from 'react-jhipster'; // eslint-disable-line

import MenuItem from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/cooperative">
        <Translate contentKey="global.menu.entities.cooperative" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/location">
        <Translate contentKey="global.menu.entities.location" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cooperative-branch">
        <Translate contentKey="global.menu.entities.cooperativeBranch" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/member">
        <Translate contentKey="global.menu.entities.member" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/individual-member">
        <Translate contentKey="global.menu.entities.individualMember" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/organization-member">
        <Translate contentKey="global.menu.entities.organizationMember" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/membership-application">
        <Translate contentKey="global.menu.entities.membershipApplication" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/social-profile">
        <Translate contentKey="global.menu.entities.socialProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/family-member">
        <Translate contentKey="global.menu.entities.familyMember" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/professional-profile">
        <Translate contentKey="global.menu.entities.professionalProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/identity-document">
        <Translate contentKey="global.menu.entities.identityDocument" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/member-document">
        <Translate contentKey="global.menu.entities.memberDocument" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/economic-activity-type">
        <Translate contentKey="global.menu.entities.economicActivityType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/economic-activity">
        <Translate contentKey="global.menu.entities.economicActivity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agricultural-activity">
        <Translate contentKey="global.menu.entities.agriculturalActivity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/crop">
        <Translate contentKey="global.menu.entities.crop" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/crop-variety">
        <Translate contentKey="global.menu.entities.cropVariety" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agricultural-production">
        <Translate contentKey="global.menu.entities.agriculturalProduction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/livestock-type">
        <Translate contentKey="global.menu.entities.livestockType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/livestock-activity">
        <Translate contentKey="global.menu.entities.livestockActivity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/livestock-production">
        <Translate contentKey="global.menu.entities.livestockProduction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/aquatic-species">
        <Translate contentKey="global.menu.entities.aquaticSpecies" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/aquaculture-activity">
        <Translate contentKey="global.menu.entities.aquacultureActivity" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/aquaculture-production">
        <Translate contentKey="global.menu.entities.aquacultureProduction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/app-user">
        <Translate contentKey="global.menu.entities.appUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cooperative-role">
        <Translate contentKey="global.menu.entities.cooperativeRole" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cooperative-user">
        <Translate contentKey="global.menu.entities.cooperativeUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/branch-user">
        <Translate contentKey="global.menu.entities.branchUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/permission">
        <Translate contentKey="global.menu.entities.permission" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/audit-log">
        <Translate contentKey="global.menu.entities.auditLog" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/number-sequence">
        <Translate contentKey="global.menu.entities.numberSequence" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
