import { IEnsegnant } from 'app/entities/ensegnant/ensegnant.model';

export interface ILienTutoriel {
  id?: number;
  descriptionLien?: string | null;
  lien?: string | null;
  enseignant?: IEnsegnant | null;
}

export class LienTutoriel implements ILienTutoriel {
  constructor(
    public id?: number,
    public descriptionLien?: string | null,
    public lien?: string | null,
    public enseignant?: IEnsegnant | null
  ) {}
}

export function getLienTutorielIdentifier(lienTutoriel: ILienTutoriel): number | undefined {
  return lienTutoriel.id;
}
