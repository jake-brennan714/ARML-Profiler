import { IArmlGame } from 'app/shared/model/arml-game.model';
import { IArmlPlayer } from 'app/shared/model/arml-player.model';

export interface IArmlGameScore {
  id?: number;
  score?: number;
  armlGame?: IArmlGame | null;
  armlPlayer?: IArmlPlayer | null;
}

export const defaultValue: Readonly<IArmlGameScore> = {};
