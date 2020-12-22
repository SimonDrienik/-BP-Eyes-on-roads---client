<?php

use App\Models\KategoriaProblemu;
use Illuminate\Database\Seeder;

class KategoriaProblemuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $kategorie = [
            [
                'nazov' => 'Stav vozovky',
            ],
            [
                'nazov' => 'Dopravne znacenie',
            ],
            [
                'nazov' => 'Kvalita opravy',
            ],
            [
                'nazov' => 'Zelen'
            ]
        ];

        foreach ($kategorie as $kategoria_problemu) {
            KategoriaProblemu::create(array(
                'nazov' => $kategoria_problemu['nazov']
            ));
        }
    }
}
