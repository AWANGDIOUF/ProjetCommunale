import { IEvenement } from 'app/entities/evenement/evenement.model';

export interface IInscription {
  id?: number;
  nomPers?: string | null;
  prenomPers?: string | null;
  emailPers?: string | null;
  telPers?: string;
  tel1Pers?: string | null;
  etatInscription?: boolean | null;
  evenement?: IEvenement | null;
}

export class Inscription implements IInscription {
  constructor(
    public id?: number,
    public nomPers?: string | null,
    public prenomPers?: string | null,
    public emailPers?: string | null,
    public telPers?: string,
    public tel1Pers?: string | null,
    public etatInscription?: boolean | null,
    public evenement?: IEvenement | null
  ) {
    this.etatInscription = this.etatInscription ?? false;
  }
}

export function getInscriptionIdentifier(inscription: IInscription): number | undefined {
  return inscription.id;
}
