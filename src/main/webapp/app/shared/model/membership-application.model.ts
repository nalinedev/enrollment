import dayjs from 'dayjs';

import { ICooperativeBranch } from 'app/shared/model/cooperative-branch.model';
import { ICooperative } from 'app/shared/model/cooperative.model';
import { MembershipApplicationStatus } from 'app/shared/model/enumerations/membership-application-status.model';
import { IMember } from 'app/shared/model/member.model';

export interface IMembershipApplication {
  id?: number;
  applicationNumber?: string;
  status?: keyof typeof MembershipApplicationStatus;
  applicationDate?: dayjs.Dayjs;
  submittedAt?: dayjs.Dayjs | null;
  reviewedAt?: dayjs.Dayjs | null;
  approvedAt?: dayjs.Dayjs | null;
  rejectedAt?: dayjs.Dayjs | null;
  rejectionReason?: string | null;
  reviewComments?: string | null;
  confirmation?: boolean;
  notes?: string | null;
  member?: IMember | null;
  cooperative?: ICooperative | null;
  branch?: ICooperativeBranch | null;
}

export const defaultValue: Readonly<IMembershipApplication> = {
  confirmation: false,
};
