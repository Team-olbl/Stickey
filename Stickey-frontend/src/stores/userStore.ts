import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export interface IPreferences {
  sportsClubId: number;
  sportsClubLogo: string;
  sportsClubName: string;
}

interface IUserState {
  isLogin: boolean;
  id: number;
  name?: string;
  email?: string;
  phone?: string;
  accessToken: string;
  refreshToken: string;
  role?: 'INDIVIDUAL' | 'ORGANIZATION';
  profile?: string;
  manager?: string;
  address?: string;
  registrationNumber?: string;
  registrationFile?: string;
  preferences: IPreferences[];
  setTokens: (accessToken: string) => void;
  loginUser: ({
    id,
    name,
    email,
    phone,
    profile,
    accessToken,
    refreshToken,
    role,
    preferences,
  }: {
    id: number;
    name?: string;
    email?: string;
    phone?: string;
    profile?: string;
    accessToken: string;
    refreshToken: string;
    role?: 'INDIVIDUAL' | 'ORGANIZATION';
    preferences: IPreferences[];
  }) => void;
  logoutUser: () => void;
  setPreference: (preference: IPreferences[]) => void;
}

const userStore = create(
  persist<IUserState>(
    set => ({
      isLogin: false,
      id: 0,
      name: '',
      email: '',
      phone: '',
      accessToken: '',
      refreshToken: '',
      role: 'INDIVIDUAL',
      profile: undefined,
      manager: undefined,
      address: undefined,
      registrationNumber: undefined,
      registrationFile: undefined,
      preferences: [],
      setTokens: accessToken => set(() => ({ accessToken })),
      loginUser: ({ id, name, email, phone, profile, accessToken, refreshToken, role, preferences }) =>
        set({
          id: id,
          name: name,
          email: email,
          phone: phone,
          profile: profile,
          accessToken: accessToken,
          refreshToken: refreshToken,
          role: role,
          preferences: preferences || [],
          isLogin: true,
        }),
      logoutUser: () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        set({
          isLogin: false,
          id: 0,
          name: '',
          email: '',
          phone: '',
          accessToken: '',
          refreshToken: '',
          role: 'INDIVIDUAL',
          profile: undefined,
          manager: undefined,
          address: undefined,
          registrationNumber: undefined,
          registrationFile: undefined,
          preferences: [],
        });
      },
      setPreference: (preferences: IPreferences[]) => set(() => ({ preferences })),
    }),
    {
      name: 'user-store',
    },
  ),
);

export default userStore;
