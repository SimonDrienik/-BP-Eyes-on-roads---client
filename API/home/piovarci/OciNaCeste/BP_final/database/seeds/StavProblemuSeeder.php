<?php

use App\Models\StavProblemu;
use Illuminate\Database\Seeder;

class StavProblemuSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $stavy = [
            [
                'nazov' => 'chybajuca',
            ],
            [
                'nazov' => 'poskodena',
            ],
            [
                'nazov' => 'vyblednuta',
            ],
            [
                'nazov' => 'zle viditelna'
            ]
        ];

        foreach ($stavy as $stavy_problemu) {
            StavProblemu::create(array(
                'nazov' => $stavy_problemu['nazov']
            ));
        }
    }
}
