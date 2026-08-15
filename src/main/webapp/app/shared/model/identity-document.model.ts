import dayjs from 'dayjs';

import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { IMember } from 'app/shared/model/member.model';

export interface IIdentityDocument {
  id?: number;
  documentType?: string;
  documentNumber?: string;
  issueDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  issuingAuthority?: string | null;
  issuingCountry?: string | null;
  status?: keyof typeof DocumentStatus;
  verified?: boolean;
  verificationDate?: dayjs.Dayjs | null;
  verificationComment?: string | null;
  member?: IMember | null;
}

export const defaultValue: Readonly<IIdentityDocument> = {
  verified: false,
};
