import dayjs from 'dayjs/esm';
import { IEntrepreneur } from 'app/entities/entrepreneur/entrepreneur.model';

export interface IDemandeInterview {
  id?: number;
  nomJournaliste?: string | null;
  prenomJournaliste?: string | null;
  nomSociete?: string;
  emailJournalite?: string;
  dateInterview?: dayjs.Dayjs | null;
  etatDemande?: boolean | null;
  entrepreneur?: IEntrepreneur | null;
}

export class DemandeInterview implements IDemandeInterview {
  constructor(
    public id?: number,
    public nomJournaliste?: string | null,
    public prenomJournaliste?: string | null,
    public nomSociete?: string,
    public emailJournalite?: string,
    public dateInterview?: dayjs.Dayjs | null,
    public etatDemande?: boolean | null,
    public entrepreneur?: IEntrepreneur | null
  ) {
    this.etatDemande = this.etatDemande ?? false;
  }
}

export function getDemandeInterviewIdentifier(demandeInterview: IDemandeInterview): number | undefined {
  return demandeInterview.id;
}
