import { IArmlGame } from 'app/shared/model/arml-game.model';
import { ArmlLeague } from 'app/shared/model/enumerations/arml-league.model';

export interface IArmlPlayer {
  id?: number;
  playerID?: number;
  firstName?: string;
  lastName?: string;
  tenhouName?: string | null;
  league?: keyof typeof ArmlLeague;
  games?: IArmlGame[] | null;
}

export const defaultValue: Readonly<IArmlPlayer> = {};
