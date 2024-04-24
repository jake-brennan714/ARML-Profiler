import { IArmlPlayer } from 'app/shared/model/arml-player.model';

export interface IArmlGame {
  id?: number;
  gameID?: number;
  players?: IArmlPlayer[] | null;
}

export const defaultValue: Readonly<IArmlGame> = {};
