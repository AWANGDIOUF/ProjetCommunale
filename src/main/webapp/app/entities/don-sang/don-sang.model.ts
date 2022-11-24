import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { IDonneur } from 'app/entities/donneur/donneur.model';

export interface IDonSang {
  id?: number;
  organisateur?: string | null;
  description?: string | null;
  annonce?: IAnnonce | null;
  donneur?: IDonneur | null;
}

export class DonSang implements IDonSang {
  constructor(
    public id?: number,
    public organisateur?: string | null,
    public description?: string | null,
    public annonce?: IAnnonce | null,
    public donneur?: IDonneur | null
  ) {}
}

export function getDonSangIdentifier(donSang: IDonSang): number | undefined {
  return donSang.id;
}
