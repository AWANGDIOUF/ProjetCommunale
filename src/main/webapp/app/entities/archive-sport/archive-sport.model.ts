import * as dayjs from 'dayjs';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { IClub } from 'app/entities/club/club.model';

export interface IArchiveSport {
  id?: number;
  annee?: dayjs.Dayjs | null;
  equipes?: IEquipe[] | null;
  clubs?: IClub[] | null;
}

export class ArchiveSport implements IArchiveSport {
  constructor(public id?: number, public annee?: dayjs.Dayjs | null, public equipes?: IEquipe[] | null, public clubs?: IClub[] | null) {}
}

export function getArchiveSportIdentifier(archiveSport: IArchiveSport): number | undefined {
  return archiveSport.id;
}
