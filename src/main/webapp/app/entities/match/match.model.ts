import dayjs from 'dayjs/esm';
import { IEquipe } from 'app/entities/equipe/equipe.model';

export interface IMatch {
  id?: number;
  dateMatch?: dayjs.Dayjs | null;
  lieuMatch?: string | null;
  scoreMatch?: number | null;
  equipe?: IEquipe | null;
}

export class Match implements IMatch {
  constructor(
    public id?: number,
    public dateMatch?: dayjs.Dayjs | null,
    public lieuMatch?: string | null,
    public scoreMatch?: number | null,
    public equipe?: IEquipe | null
  ) {}
}

export function getMatchIdentifier(match: IMatch): number | undefined {
  return match.id;
}
