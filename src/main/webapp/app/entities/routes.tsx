import React from 'react';
import { Route } from 'react-router'; // eslint-disable-line

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AgriculturalActivity from './agricultural-activity';
import AgriculturalProduction from './agricultural-production';
import AppUser from './app-user';
import AquacultureActivity from './aquaculture-activity';
import AquacultureProduction from './aquaculture-production';
import AquaticSpecies from './aquatic-species';
import AuditLog from './audit-log';
import BranchUser from './branch-user';
import Cooperative from './cooperative';
import CooperativeBranch from './cooperative-branch';
import IndividualMember from './individual-member';
import Location from './location';
import Member from './member';
import MembershipApplication from './membership-application';
import NumberSequence from './number-sequence';
import OrganizationMember from './organization-member';
import SocialProfile from './social-profile';
import FamilyMember from './family-member';
import ProfessionalProfile from './professional-profile';
import IdentityDocument from './identity-document';
import MemberDocument from './member-document';
import EconomicActivityType from './economic-activity-type';
import EconomicActivity from './economic-activity';
import Crop from './crop';
import CropVariety from './crop-variety';
import LivestockType from './livestock-type';
import LivestockActivity from './livestock-activity';
import LivestockProduction from './livestock-production';
import CooperativeRole from './cooperative-role';
import CooperativeUser from './cooperative-user';
import Permission from './permission';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="/cooperative/*" element={<Cooperative />} />
        <Route path="/location/*" element={<Location />} />
        <Route path="/cooperative-branch/*" element={<CooperativeBranch />} />
        <Route path="/member/*" element={<Member />} />
        <Route path="/individual-member/*" element={<IndividualMember />} />
        <Route path="/organization-member/*" element={<OrganizationMember />} />
        <Route path="/membership-application/*" element={<MembershipApplication />} />
        <Route path="/social-profile/*" element={<SocialProfile />} />
        <Route path="/family-member/*" element={<FamilyMember />} />
        <Route path="/professional-profile/*" element={<ProfessionalProfile />} />
        <Route path="/identity-document/*" element={<IdentityDocument />} />
        <Route path="/member-document/*" element={<MemberDocument />} />
        <Route path="/economic-activity-type/*" element={<EconomicActivityType />} />
        <Route path="/economic-activity/*" element={<EconomicActivity />} />
        <Route path="/agricultural-activity/*" element={<AgriculturalActivity />} />
        <Route path="/crop/*" element={<Crop />} />
        <Route path="/crop-variety/*" element={<CropVariety />} />
        <Route path="/agricultural-production/*" element={<AgriculturalProduction />} />
        <Route path="/livestock-type/*" element={<LivestockType />} />
        <Route path="/livestock-activity/*" element={<LivestockActivity />} />
        <Route path="/livestock-production/*" element={<LivestockProduction />} />
        <Route path="/aquatic-species/*" element={<AquaticSpecies />} />
        <Route path="/aquaculture-activity/*" element={<AquacultureActivity />} />
        <Route path="/aquaculture-production/*" element={<AquacultureProduction />} />
        <Route path="/app-user/*" element={<AppUser />} />
        <Route path="/cooperative-role/*" element={<CooperativeRole />} />
        <Route path="/cooperative-user/*" element={<CooperativeUser />} />
        <Route path="/branch-user/*" element={<BranchUser />} />
        <Route path="/permission/*" element={<Permission />} />
        <Route path="/audit-log/*" element={<AuditLog />} />
        <Route path="/number-sequence/*" element={<NumberSequence />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
