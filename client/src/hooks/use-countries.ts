import { useQuery } from "@tanstack/react-query";

interface Country {
  name: {
    common: string;
  };
  cca2: string; // ISO 2-letter code
}

async function fetchCountries(): Promise<Country[]> {
  const res = await fetch("https://restcountries.com/v3.1/all?fields=name,cca2");
  if (!res.ok) throw new Error("Failed to fetch countries");
  return res.json();
}

export interface CountryOption {
  code: string;
  name: string;
}

export function useCountries() {
  return useQuery({
    queryKey: ["countries"],
    queryFn: async (): Promise<CountryOption[]> => {
      const data = await fetchCountries();
      return data
        .sort((a, b) => a.name.common.localeCompare(b.name.common))
        .map((c) => ({ code: c.cca2, name: c.name.common }));
    },
    staleTime: 1000 * 60 * 60 * 24, // 24 hours
  });
}
