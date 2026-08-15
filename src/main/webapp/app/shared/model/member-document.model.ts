import dayjs from 'dayjs';

import { IAppUser } from 'app/shared/model/app-user.model';
import { DocumentVerificationStatus } from 'app/shared/model/enumerations/document-verification-status.model';
import { MemberDocumentType } from 'app/shared/model/enumerations/member-document-type.model';
import { IMember } from 'app/shared/model/member.model';

export interface IMemberDocument {
  id?: number;
  documentType?: keyof typeof MemberDocumentType;
  originalFileName?: string | null;
  storedFileName?: string | null;
  contentType?: string | null;
  fileSize?: number | null;
  storagePath?: string | null;
  checksum?: string | null;
  verificationStatus?: keyof typeof DocumentVerificationStatus;
  uploadedAt?: dayjs.Dayjs;
  verifiedAt?: dayjs.Dayjs | null;
  notes?: string | null;
  member?: IMember | null;
  uploadedBy?: IAppUser | null;
}

export const defaultValue: Readonly<IMemberDocument> = {};
