import dayjs from 'dayjs/esm';
import { IArtiste } from 'app/entities/artiste/artiste.model';

export interface IEvenement {
  id?: number;
  nomEvenement?: string | null;
  libelle?: string | null;
  action?: string | null;
  decision?: string | null;
  delaiInstruction?: dayjs.Dayjs | null;
  delaiInscription?: dayjs.Dayjs | null;
  delaiValidation?: dayjs.Dayjs | null;
  artiste?: IArtiste | null;
}

export class Evenement implements IEvenement {
  constructor(
    public id?: number,
    public nomEvenement?: string | null,
    public libelle?: string | null,
    public action?: string | null,
    public decision?: string | null,
    public delaiInstruction?: dayjs.Dayjs | null,
    public delaiInscription?: dayjs.Dayjs | null,
    public delaiValidation?: dayjs.Dayjs | null,
    public artiste?: IArtiste | null
  ) {}
}

export function getEvenementIdentifier(evenement: IEvenement): number | undefined {
  return evenement.id;
}
