import * as dayjs from 'dayjs';
import { IEquipe } from 'app/entities/equipe/equipe.model';

export interface IMatch {
  id?: number;
  date?: dayjs.Dayjs | null;
  lieu?: string | null;
  score?: number | null;
  equipes?: IEquipe[] | null;
}

export class Match implements IMatch {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public lieu?: string | null,
    public score?: number | null,
    public equipes?: IEquipe[] | null
  ) {}
}

export function getMatchIdentifier(match: IMatch): number | undefined {
  return match.id;
}
