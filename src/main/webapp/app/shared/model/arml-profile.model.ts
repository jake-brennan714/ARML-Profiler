import { IUser } from 'app/shared/model/user.model';
import { IArmlPlayer } from 'app/shared/model/arml-player.model';

export interface IArmlProfile {
  id?: number;
  winRate?: number | null;
  feedRate?: number | null;
  callRate?: number | null;
  riiRate?: number | null;
  feedEV?: number | null;
  user?: IUser | null;
  playerID?: IArmlPlayer | null;
}

export const defaultValue: Readonly<IArmlProfile> = {};
