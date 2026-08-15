import dayjs from 'dayjs';

import { ICooperativeBranch } from 'app/shared/model/cooperative-branch.model';
import { ICooperative } from 'app/shared/model/cooperative.model';
import { MemberStatus } from 'app/shared/model/enumerations/member-status.model';
import { MemberType } from 'app/shared/model/enumerations/member-type.model';
import { IIndividualMember } from 'app/shared/model/individual-member.model';
import { IOrganizationMember } from 'app/shared/model/organization-member.model';
import { IProfessionalProfile } from 'app/shared/model/professional-profile.model';
import { ISocialProfile } from 'app/shared/model/social-profile.model';

export interface IMember {
  id?: number;
  memberNumber?: string;
  memberType?: keyof typeof MemberType;
  status?: keyof typeof MemberStatus;
  admissionDate?: dayjs.Dayjs | null;
  exitDate?: dayjs.Dayjs | null;
  exitReason?: string | null;
  notes?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  individualMember?: IIndividualMember | null;
  organizationMember?: IOrganizationMember | null;
  socialProfile?: ISocialProfile | null;
  professionalProfile?: IProfessionalProfile | null;
  cooperative?: ICooperative | null;
  branch?: ICooperativeBranch | null;
}

export const defaultValue: Readonly<IMember> = {};
