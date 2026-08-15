import dayjs from 'dayjs';

import { IAppUser } from 'app/shared/model/app-user.model';
import { AuditAction } from 'app/shared/model/enumerations/audit-action.model';

export interface IAuditLog {
  id?: number;
  action?: keyof typeof AuditAction;
  entityName?: string;
  entityId?: string | null;
  username?: string | null;
  cooperativeId?: number | null;
  branchId?: number | null;
  timestamp?: dayjs.Dayjs;
  ipAddress?: string | null;
  userAgent?: string | null;
  oldValue?: string | null;
  newValue?: string | null;
  description?: string | null;
  appUser?: IAppUser | null;
}

export const defaultValue: Readonly<IAuditLog> = {};
