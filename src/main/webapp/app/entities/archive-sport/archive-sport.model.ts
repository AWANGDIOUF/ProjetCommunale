import dayjs from 'dayjs/esm';
import { IEquipe } from 'app/entities/equipe/equipe.model';
import { IClub } from 'app/entities/club/club.model';

export interface IArchiveSport {
  id?: number;
  annee?: dayjs.Dayjs | null;
  equipe?: IEquipe | null;
  club?: IClub | null;
}

export class ArchiveSport implements IArchiveSport {
  constructor(public id?: number, public annee?: dayjs.Dayjs | null, public equipe?: IEquipe | null, public club?: IClub | null) {}
}

export function getArchiveSportIdentifier(archiveSport: IArchiveSport): number | undefined {
  return archiveSport.id;
}
