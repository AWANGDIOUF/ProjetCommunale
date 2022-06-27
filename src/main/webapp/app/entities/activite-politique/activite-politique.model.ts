import dayjs from 'dayjs/esm';

export interface IActivitePolitique {
  id?: number;
  titreActivite?: string | null;
  descriptionActivite?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
}

export class ActivitePolitique implements IActivitePolitique {
  constructor(
    public id?: number,
    public titreActivite?: string | null,
    public descriptionActivite?: string | null,
    public dateDebut?: dayjs.Dayjs | null,
    public dateFin?: dayjs.Dayjs | null
  ) {}
}

export function getActivitePolitiqueIdentifier(activitePolitique: IActivitePolitique): number | undefined {
  return activitePolitique.id;
}
