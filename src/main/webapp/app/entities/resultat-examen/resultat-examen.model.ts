import dayjs from 'dayjs/esm';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { Examen } from 'app/entities/enumerations/examen.model';

export interface IResultatExamen {
  id?: number;
  typeExament?: Examen | null;
  autreExamen?: string | null;
  tauxReuissite?: number | null;
  annee?: dayjs.Dayjs | null;
  etablissement?: IEtablissement | null;
}

export class ResultatExamen implements IResultatExamen {
  constructor(
    public id?: number,
    public typeExament?: Examen | null,
    public autreExamen?: string | null,
    public tauxReuissite?: number | null,
    public annee?: dayjs.Dayjs | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getResultatExamenIdentifier(resultatExamen: IResultatExamen): number | undefined {
  return resultatExamen.id;
}
